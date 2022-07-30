package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Inhibitor;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

@ModuleManifest(
        name = "AutoCrystal",
        category = Module.Category.COMBAT,
        description = "Places and breaks crystals"
)
public class AutoCrystal extends Module {

    //Interaction && Sync
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Fast, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<swing> swingMode = new Setting<>("Swing", swing.Both, this).setVisibility(v-> interaction.getValue());
    public enum swing{
        Main, Off, Both, Packet
    }
    public Setting<blow> blowMode = new Setting<>("Blow", blow.Controller, this).setVisibility(v-> interaction.getValue());
    public enum blow{
        Packet, Controller
    }
    public Setting<Boolean> rayTrace = new Setting<>("RaytraceCheck", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", false, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> sync = new Setting<>("Sync", true, this).setVisibility(v-> interaction.getValue());

    //Ranges
    public Setting<Boolean> ranges = new Setting<>("Ranges", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 5, this, 0, 10).setVisibility(v-> ranges.getValue());
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());
    public Setting<Integer> breakRange = new Setting<>("BreakRange", 5, this, 0, 6).setVisibility(v-> ranges.getValue());

    //Damages
    public Setting<Boolean> damages = new Setting<>("Damages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 15, this, 0, 36).setVisibility(v-> damages.getValue());
    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 1, this, 0, 36).setVisibility(v-> damages.getValue());

    //Delays
    public Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 80, this, 0, 200);
    public Setting<Integer> breakDelay = new Setting<>("BreakDelayMs", 70, this, 0, 200);

    //Inhibition
    public Setting<Boolean> inhibit = new Setting<>("Inhibit", true, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> shouldInhibit = new Setting<>("ShouldInhibit", true, this).setVisibility(v-> inhibit.getValue());
    public Setting<Integer> minPercent = new Setting<>("MinPercent", 0, this, 0, 100).setVisibility(v-> shouldInhibit.getValue() && inhibit.getValue());
    public Setting<Integer> maxPercent = new Setting<>("MaxPercent", 0, this, 0, 100).setVisibility(v-> shouldInhibit.getValue() && inhibit.getValue());
    public Setting<Integer> speed = new Setting<>("Speed", 0, this, 0, 100).setVisibility(v-> shouldInhibit.getValue() && inhibit.getValue());
    public Setting<Integer> inhibitPercent = new Setting<>("InhibitPercent", 0, this, 0, 100).setVisibility(v-> shouldInhibit.getValue() && inhibit.getValue());

    //Render
    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);

    EntityPlayer target;

    BlockPos placePos;

    Inhibitor inhibitor;
    Timer timer;
    ItemUser user;

    int stage;

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketSoundEffect && sync.getValue()) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getSound().equals(SoundEvents.ENTITY_GENERIC_EXPLODE) && packet.getCategory().equals(SoundCategory.BLOCKS)) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || entity.isDead)
                        continue;
                    if (entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6)
                        entity.setDead();
                }
            }
        }
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51 && placePos != null && placePos.equals(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ())))
                stage = 2;
        }
    });

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketUseEntity && (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(AutoCrystal.mc.world) instanceof EntityEnderCrystal && cancelCrystal.getValue())) {
            Objects.requireNonNull(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world)).setDead();
            mc.world.removeEntityFromWorld(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world).getEntityId());
        }
    });

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) super.onDisable();
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        inhibitor = new Inhibitor();
        timer = new Timer();
        user = new ItemUser(this, switchMode, rotate);

        stage = autoStage();
        inhibitor.value = minPercent.getValue();
        inhibitor.setValues(minPercent.getValue(), maxPercent.getValue(), speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        user = null;
        timer = null;
        inhibitor = null;
        target = null;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(worldRender3DEvent -> {
        if(placePos == null) return;
        new BlockRenderBuilder(placePos)
                .addRenderModes(
                        new RenderMode(RenderMode.renderModes.Fill, color.getValue().toRGB())
                ).render();
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        //Updates inhibitor
        if (inhibitor != null) {
            inhibitor.setValues(minPercent.getValue(), maxPercent.getValue(), speed.getValue());
            inhibitor.update();
            inhibitPercent.setValue((int) Math.round(inhibitor.value));
        }

        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;
        if(stage == 1 && placePos == null)
            stage = 2;

        switch (stage){
            case 1:
                break;
            case 2:
                //Breakes a crystal
                EntityEnderCrystal breakCrystal = CrystalUtils.getBestCrystal(target, breakRange.getValue(), rayTrace.getValue());
                if(breakCrystal != null){
                    //Returns break delay, if inhibitor is turned returns simple break delay, else break delay * current inhibit percent
                    int tempBreakDelay = (shouldInhibit.getValue() && !maxPercent.getValue(0) && !inhibitPercent.getValue(0)) ? Math.round(breakDelay.getValue() * maxPercent.getValue() / inhibitPercent.getValue()) : breakDelay.getValue();
                    if(timer.hasPassedMs(tempBreakDelay)){
                        Firework.rotationManager.rotateSpoof(breakCrystal.getPositionVector().add(0.5, 0.5, 0.5));
                        //Blows
                        if(blowMode.getValue(blow.Controller))
                            mc.playerController.attackEntity(mc.player, breakCrystal);
                        else if(blowMode.getValue(blow.Packet))
                            mc.getConnection().sendPacket(new CPacketUseEntity(breakCrystal));

                        //Hand swing
                        swing(swingMode.getValue());
                        stage = 3;
                        timer.reset();
                    }
                }else
                    stage = 3;
                break;
            case 3:
                //Places a crystal
                BlockPos toPlace = CrystalUtils.bestCrystalPos(target, placeRange.getValue(), true, maxSelfDmg.getValue(), minTargetDmg.getValue(), rayTrace.getValue());
                placePos = toPlace;
                if(toPlace != null){
                    //Returns place delay, if inhibitor is turned returns simple place delay, else place delay * current inhibit percent
                    int tempPlaceDelay = (shouldInhibit.getValue() && !maxPercent.getValue(0) && !inhibitPercent.getValue(0)) ? Math.round(placeDelay.getValue() * maxPercent.getValue() / inhibitPercent.getValue()) : placeDelay.getValue();
                    if(timer.hasPassedMs(tempPlaceDelay)){
                        user.useItem(Items.END_CRYSTAL, toPlace, EnumHand.MAIN_HAND);
                        stage = 1;
                        timer.reset();
                    }
                }else
                    stage = 1;
                break;
        }
    });

    public int autoStage(){
        EntityEnderCrystal breakCrystal = CrystalUtils.getBestCrystal(target, breakRange.getValue(), rayTrace.getValue());
        if(breakCrystal != null)
            return 2;
        BlockPos toPlace = CrystalUtils.bestCrystalPos(target, placeRange.getValue(), true, maxSelfDmg.getValue(), minTargetDmg.getValue(), rayTrace.getValue());
        if(toPlace != null)
            return 3;

        return 2;
    }

    public void swing(swing swing) {
        switch (swing) {
            case Main:
                //Swings main hand
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case Off:
                //Swings offhand
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case Both:
                //Swings both hands
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case Packet:
                //Sends a swing packet
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                break;
        }
    }
}

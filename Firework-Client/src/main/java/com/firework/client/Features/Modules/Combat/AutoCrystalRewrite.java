package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.*;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Objects;

@ModuleManifest(name = "AutoCrystalRewrite", category = Module.Category.COMBAT)
public class AutoCrystalRewrite extends Module {

    //Interaction && Sync
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this).setVisibility(v-> interaction.getValue());

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Integer> rotationSpoofsLimit = new Setting<>("RotationSpoofs", 0, this, 0, 20).setVisibility(v-> interaction.getValue());

    //Swing
    public Setting<Boolean> shouldSwing = new Setting<>("Swing", true, this).setVisibility(v-> interaction.getValue());
    public Setting<swing> swingMode = new Setting<>("SwingMode", swing.Both, this).setVisibility(v-> interaction.getValue());
    public enum swing{
        Main, Off, Both, Packet
    }

    //Blow
    public Setting<blow> blowMode = new Setting<>("Blow", blow.Controller, this).setVisibility(v-> interaction.getValue());
    public enum blow{
        Packet, Controller
    }
    public Setting<Boolean> placeRayTraceResult = new Setting<>("PlaceRayTraceResult", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rayTrace = new Setting<>("RaytraceCheck", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", false, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> sync = new Setting<>("Sync", true, this).setVisibility(v-> interaction.getValue());

    //FacePlace / FaceBreak
    public Setting<Boolean> facePlBr = new Setting<>("FacePlace/Break", false, this);
    public Setting<Boolean> facePlace = new Setting<>("FacePlace", false, this).setVisibility(v-> facePlBr.getValue());
    public Setting<Boolean> faceBreak = new Setting<>("FaceBreak", false, this).setVisibility(v-> facePlBr.getValue());
    public Setting<Integer> targetHealth = new Setting<>("MinTargetHealth", 12, this, 0, 36).setVisibility(v-> facePlace.getValue() || faceBreak.getValue());

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

    //Stuff
    public Setting<Boolean> pauseWhileEating = new Setting<>("PauseWhileEating", true, this).setVisibility(v-> shouldInhibit.getValue() && inhibit.getValue());

    //Render
    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 50, 50), this);
    EntityPlayer target;

    BlockPos renderPlacePos;

    Inhibitor inhibitor;
    Timer timer;

    int stage;

    //Rotate stuff
    public Vec3d rotationVec;
    public boolean canRotate = false;
    public int rotationsSpoofed = 0;

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
    });

    @Subscribe(priority = Listener.Priority.HIGHEST)
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketUseEntity && (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(AutoCrystal.mc.world) instanceof EntityEnderCrystal && cancelCrystal.getValue())) {
            Objects.requireNonNull(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world)).setDead();
            mc.world.removeEntityFromWorld(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(mc.world).getEntityId());
        }
        if(event.getPacket() instanceof CPacketPlayer && canRotate){
            float rotations[] = RotationUtil.getRotations(rotationVec);
            ((ICPacketPlayer)event.getPacket()).setYaw(rotations[0]);
            ((ICPacketPlayer)event.getPacket()).setPitch(rotations[1]);
            rotationsSpoofed++;
            if(rotationsSpoofed >= rotationSpoofsLimit.getValue()) {
                canRotate = false;
                rotationsSpoofed = 0;
            }
        }
    });

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) super.onDisable();
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        inhibitor = new Inhibitor();
        timer = new Timer();

        stage = 1;
        inhibitor.value = minPercent.getValue();
        inhibitor.setValues(minPercent.getValue(), maxPercent.getValue(), speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer = null;
        inhibitor = null;
        target = null;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(worldRender3DEvent -> {
        if(renderPlacePos == null) return;
        new BlockRenderBuilder(renderPlacePos)
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

        //Is eating check
        if(pauseWhileEating.getValue() && (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFood || mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemFood) && mc.gameSettings.keyBindUseItem.isKeyDown()) return;

        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        //Returns break delay, if inhibitor is turned returns simple break delay, else break delay * current inhibit percent
        int tempBreakDelay = (shouldInhibit.getValue() && !maxPercent.getValue(0) && !inhibitPercent.getValue(0)) ? Math.round(breakDelay.getValue() * maxPercent.getValue() / inhibitPercent.getValue()) : breakDelay.getValue();
        //Returns place delay, if inhibitor is turned returns simple place delay, else place delay * current inhibit percent
        int tempPlaceDelay = (shouldInhibit.getValue() && !maxPercent.getValue(0) && !inhibitPercent.getValue(0)) ? Math.round(placeDelay.getValue() * maxPercent.getValue() / inhibitPercent.getValue()) : placeDelay.getValue();

        BlockPos placePos = CrystalUtils.bestCrystalPos(target, placeRange.getValue(), true, maxSelfDmg.getValue(), minTargetDmg.getValue(), rayTrace.getValue());

        EntityEnderCrystal crystal = CrystalUtils.getBestCrystal(target, breakRange.getValue(), rayTrace.getValue());

        switch(stage){
            case 1:
                if (isValidBlockPos(placePos)) {
                    if (timer.hasPassedMs(tempPlaceDelay)) {
                        boolean flag = false;
                        if (mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
                            flag = true;
                            if (!autoSwitch.getValue())
                                return;
                        }
                        if (flag) {
                            final int slot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                            if (slot == -1) return;
                            mc.player.inventory.currentItem = slot;
                            mc.playerController.updateController();
                        }

                        //Facing
                        EnumFacing facing = EnumFacing.UP;

                        boolean shouldRotate = true;

                        //RayTrace result
                        if(placeRayTraceResult.getValue()) {
                            RayTraceResult result = mc.world.rayTraceBlocks(
                                    new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
                                    new Vec3d(placePos.getX() + 0.5, placePos.getY() - 0.5, placePos.getZ() + 0.5));

                            if (result != null && result.sideHit != null) {
                                facing = result.sideHit;
                                rotate(result.hitVec);
                                shouldRotate = false;
                            }
                        }

                        if(shouldRotate)
                            rotate(new Vec3d(placePos.getX() + 0.5, placePos.getY() - 0.5, placePos.getZ() + 0.5));

                        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, facing, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));

                        //Swing
                        if (shouldSwing.getValue())
                            swing(swingMode.getValue());

                        stage = 2;
                        timer.reset();

                        renderPlacePos = placePos;
                    }
                }else
                    stage = 2;
                break;
            case 2:
                if (isValidCrystal(crystal)){
                    if(timer.hasPassedMs(tempBreakDelay)){

                        if(rotate.getValue())
                            rotate(crystal.getPositionVector());

                        //Blows
                        if(blowMode.getValue(blow.Controller))
                            mc.playerController.attackEntity(mc.player, crystal);
                        else if(blowMode.getValue(blow.Packet))
                            mc.getConnection().sendPacket(new CPacketUseEntity(crystal));

                        //Swing
                        if(shouldSwing.getValue())
                            swing(swingMode.getValue());

                        stage = 1;
                        timer.reset();
                    }
                }else
                    stage = 1;

                break;
        }

    });

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

    public void rotate(Vec3d vec3d){
        rotationVec = vec3d;
        canRotate = true;
        rotationsSpoofed = 0;
    }

    public boolean isValidCrystal(EntityEnderCrystal crystal){
        return crystal != null && (
                faceBreak.getValue() || ((target.getPosition().getY() + 1 != crystal.getPosition().getY()) && target.getHealth() <= targetHealth.getValue()));
    }

    public boolean isValidBlockPos(BlockPos pos){
        return pos != null && (
                facePlace.getValue() || ((target.getPosition().getY() != pos.getY()) && target.getHealth() <= targetHealth.getValue()));
    }
}

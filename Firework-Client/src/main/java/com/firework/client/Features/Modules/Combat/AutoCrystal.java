package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Inhibitor;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;

import java.util.ArrayList;
import java.util.Objects;

@ModuleManifest(name = "AutoCrystal", category = Module.Category.COMBAT)
public class AutoCrystal extends Module {

    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<swing> swingMode = new Setting<>("Swing", swing.Both, this).setVisibility(v-> interaction.getValue());
    public enum swing{
        Main, Off, Both, Packet
    }
    public Setting<blow> blowMode = new Setting<>("Blow", blow.Controller, this).setVisibility(v-> interaction.getValue());
    public enum blow{
        Packet, Controller
    }
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", true, this).setVisibility(v-> interaction.getValue());

    public Setting<Boolean> ranges = new Setting<>("Ranges", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 0, this, 0, 10).setVisibility(v-> ranges.getValue());
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 0, this, 0, 6).setVisibility(v-> ranges.getValue());
    public Setting<Integer> breakRange = new Setting<>("BreakRange", 0, this, 0, 6).setVisibility(v-> ranges.getValue());

    public Setting<Boolean> damages = new Setting<>("Damages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 0, this, 0, 36).setVisibility(v-> damages.getValue());
    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 0, this, 0, 36).setVisibility(v-> damages.getValue());

    public Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 0, this, 0, 200);
    public Setting<Integer> breakDelay = new Setting<>("BreakDelayMs", 0, this, 0, 200);
    public Setting<Boolean> shouldInhibit = new Setting<>("Inhibitor", true, this);
    public Setting<Integer> minDelay = new Setting<>("MinDelay", 0, this, 0, 200).setVisibility(v-> shouldInhibit.getValue());
    public Setting<Integer> maxDelay = new Setting<>("MaxDelay", 0, this, 0, 200).setVisibility(v-> shouldInhibit.getValue());

    ArrayList<BlockPos> placed;

    EntityPlayer target;

    Inhibitor inhibitor;
    Timer timer;
    ItemUser user;

    int stage;

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketUseEntity && (((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(AutoCrystal.mc.world) instanceof EntityEnderCrystal && cancelCrystal.getValue())) {
            final World world;
            Objects.requireNonNull(((CPacketUseEntity )event.getPacket()).getEntityFromWorld(AutoCrystal.mc.world)).setDead();
            AutoCrystal.mc.world.removeEntityFromWorld(((CPacketUseEntity )event.getPacket()).getEntityFromWorld((World) mc.world).getEntityId());
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) onDisable();

        placed = new ArrayList<>();
        target = PlayerUtil.getClosestTarget(targetRange.getValue());
        inhibitor = new Inhibitor();
        timer = new Timer();
        user = new ItemUser(this, switchMode, rotate);

        stage = 1;
        Firework.eventBus.register(listener1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Firework.eventBus.unregister(listener1);
        user = null;
        timer = null;
        inhibitor = null;
        target = null;
        placed = null;
    }

    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;

        if(target == null) return;
        switch (stage){
            case 1:
                //Breakes a crystal
                EntityEnderCrystal breakCrystal = CrystalUtils.getBestCrystal(target, breakRange.getValue());
                if(breakCrystal != null){
                    if(timer.hasPassedMs(breakDelay.getValue())){
                        Firework.rotationManager.rotateSpoof(breakCrystal.getPositionVector().add(0.5, 0.5, 0.5));
                        //Blows
                        if(blowMode.getValue(blow.Controller))
                            mc.playerController.attackEntity(mc.player, breakCrystal);
                        else if(blowMode.getValue(blow.Packet))
                            mc.getConnection().sendPacket(new CPacketUseEntity(breakCrystal));

                        //Hand swing
                        swing(swingMode.getValue());
                        stage = 2;
                        timer.reset();
                    }
                }else
                    stage = 2;
                break;
            case 2:
                //Places a crystal
                BlockPos toPlace = CrystalUtils.bestCrystalPos(target, placeRange.getValue(), true, maxSelfDmg.getValue(), minTargetDmg.getValue());
                if(toPlace != null){
                    if(timer.hasPassedMs(placeDelay.getValue())){
                        user.useItem(Items.END_CRYSTAL, toPlace, EnumHand.MAIN_HAND);
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
}

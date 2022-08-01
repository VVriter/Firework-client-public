package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import com.firework.client.Implementations.Utill.Inhibitor;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Objects;

@ModuleManifest(name = "AutoCrystalRewrite2", category = Module.Category.COMBAT)
public class AutoCrystalRewrite2 extends Module {
    //Interaction && Sync
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this).setVisibility(v-> interaction.getValue());

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Integer> rotationSpoofsLimit = new Setting<>("RotationSpoofs", 0, this, 0, 20).setVisibility(v-> interaction.getValue());

    //Swing
    public Setting<Boolean> shouldSwing = new Setting<>("Swing", true, this).setVisibility(v-> interaction.getValue());
    public Setting<AutoCrystalRewrite.swing> swingMode = new Setting<>("SwingMode", AutoCrystalRewrite.swing.Both, this).setVisibility(v-> interaction.getValue());
    public enum swing{
        Main, Off, Both, Packet
    }

    //Blow
    public Setting<AutoCrystalRewrite.blow> blowMode = new Setting<>("Blow", AutoCrystalRewrite.blow.Controller, this).setVisibility(v-> interaction.getValue());
    public enum blow{
        Packet, Controller
    }
    public Setting<Boolean> placeRayTraceResult = new Setting<>("PlaceRayTraceResult", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rayTrace = new Setting<>("RaytraceCheck", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> cancelCrystal = new Setting<>("CancelCrystal", false, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> sync = new Setting<>("Sync", true, this).setVisibility(v-> interaction.getValue());

    //FacePlace / FaceBreak
    public Setting<Boolean> facePlBr = new Setting<>("FacePlace/Break", false, this).setMode(Setting.Mode.SUB);
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
    public Setting<Boolean> noSuicide = new Setting<>("NoSuicide", true, this);
    public Setting<Integer> noSuicidePlHealth = new Setting<>("SelfHealth", 10, this, 0, 36).setVisibility(v-> noSuicide.getValue());
    public Setting<Boolean> pauseWhileEating = new Setting<>("PauseWhileEating", true, this);

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

    //Updates inhibitor
    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> inhibition = new Listener<>(event -> {
        if(fullNullCheck()) return;

        //Updates inhibitor
        if (inhibitor != null) {
            inhibitor.setValues(minPercent.getValue(), maxPercent.getValue(), speed.getValue());
            inhibitor.update();
            inhibitPercent.setValue((int) Math.round(inhibitor.value));
        }
    });

    //Place && Break logic listener
    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> logic = new Listener<>(event -> {
        
    });

    public void rotate(Vec3d vec3d){
        rotationVec = vec3d;
        canRotate = true;
        rotationsSpoofed = 0;
    }

    public boolean isValidCrystal(EntityEnderCrystal crystal){
        return crystal != null
                && (faceBreak.getValue() && ((target.getPosition().getY()+1 == crystal.getPosition().getY() && target.getHealth() <= targetHealth.getValue()) || ((target.getPosition().getY() + 1 != crystal.getPosition().getY())))
                && (!noSuicide.getValue() || (mc.player.getHealth() - CrystalUtil.calculateDamage(crystal, mc.player) > noSuicidePlHealth.getValue())));
    }

    public boolean isValidBlockPos(BlockPos pos){
        return pos != null
                && (facePlace.getValue() && ((target.getPosition().getY() == pos.getY() && target.getHealth() <= targetHealth.getValue()) || (target.getPosition().getY() != pos.getY()))
                && (!noSuicide.getValue() || (mc.player.getHealth() - CrystalUtil.calculateDamage(pos, mc.player) > noSuicidePlHealth.getValue())));
    }
}

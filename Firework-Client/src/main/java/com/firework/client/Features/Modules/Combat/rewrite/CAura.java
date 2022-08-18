package com.firework.client.Features.Modules.Combat.rewrite;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtils;
import com.firework.client.Implementations.Utill.Entity.TargetUtil;
import com.firework.client.Implementations.Utill.Render.RenderEntityUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "CAura",
        category = Module.Category.COMBAT,
        description = ""
)

public class CAura extends Module {

    public Setting<Double> targetRange = new Setting<>("TargetRange", (double)4, this, 1, 5);
    public Setting<Boolean> rotationSubBool = new Setting<>("Rotations", false, this).setMode(Setting.Mode.SUB);
    public Setting<RotationMode> rotationMode = new Setting<>("Mode", RotationMode.Instant, this).setVisibility(()-> rotationSubBool.getValue());
    public enum RotationMode{
        Instant, YawStep, None
    }
    public Setting<Boolean> placeSubBool = new Setting<>("Place", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> placeRange = new Setting<>("PlaceRange", 3, this, 1, 6).setVisibility(()-> placeSubBool.getValue());
    public Setting<Integer> placeDelay= new Setting<>("PlaceDelay", 200, this, 1, 1000).setVisibility(()-> placeSubBool.getValue());

    public Setting<Boolean> breakSubBool = new Setting<>("Break", false, this).setMode(Setting.Mode.SUB);
    public Setting<Integer> breakRange = new Setting<>("BreakRange", 3, this, 1, 6).setVisibility(()-> breakSubBool.getValue());
    public Setting<Integer> breakDelay= new Setting<>("BreakDelay", 200, this, 1, 1000).setVisibility(()-> breakSubBool.getValue());


    public Setting<Boolean> damagesSubBool = new Setting<>("Damages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Double> maxSelfDmg = new Setting<>("MaxSelfDmg", (double)15, this, 1, 26).setVisibility(()-> damagesSubBool.getValue());
    public Setting<Double> minTargetDmg = new Setting<>("MinTargetDmg", (double)1, this, 1, 26).setVisibility(()-> damagesSubBool.getValue());

    Entity target;
    BlockPos posToPlace;
    Vec3d posToRotate;

    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();

    @Override
    public void onToggle() {
        super.onToggle();
        placeTimer.reset();
        breakTimer.reset();
    }

        @Subscribe
        public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(e-> {
            target = TargetUtil.getClosest(true,false,false,false,false, targetRange.getValue());
            if (target == null) return;

            posToPlace = CrystalUtils.bestCrystalPos((EntityPlayer) target,placeRange.getValue(),maxSelfDmg.getValue().floatValue(),minTargetDmg.getValue().floatValue());

            if (posToPlace == null) return;
            posToRotate = new Vec3d(posToPlace.getX(), posToPlace.getY(), posToPlace.getZ());

            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(posToPlace.add(0, 1, 0)))) {
                if (!entity.isDead && entity instanceof EntityEnderCrystal ) {
                    if (breakTimer.hasPassedMs(breakDelay.getValue())) {
                        Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, entity);
                        Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                        breakTimer.reset();
                    }
                }
            }

            if (CrystalUtils.canPlaceCrystal(posToPlace)){
                if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                    BlockUtil.placeCrystalOnBlock(posToPlace,EnumHand.MAIN_HAND,true,true, false);
                    placeTimer.reset();
                }
            }


            switch (rotationMode.getValue()) {
                case None:
                    //Empty block
                    break;
                case Instant:
                    //Rotations
                    float[] rotate = RotationUtil.getRotations(posToRotate);
                    RotationUtil.packetFacePitchAndYaw(rotate[1],rotate[0]);
                    break;
                case YawStep:
                    //Rotations
                    break;
            }
        });

        @Subscribe
        public Listener<Render3dE> ex = new Listener<>(e-> {
            if (target == null) return;
            if (posToPlace == null) return;
            RenderEntityUtils.drawEntityBox(target, Color.WHITE, true,160,1);
            RenderUtils.drawBoxESP(posToPlace,Color.WHITE,1,true,true,255,1);
        });
}

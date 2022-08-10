package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "AuraRewrite",
        category = Module.Category.COMBAT
)
public class AuraRewrite extends Module {

    public Setting<Double> targetRange = new Setting<>("TargetRange", (double)4, this, 0, 6);

    public Setting<Boolean> rotationsSubBool = new Setting<>("Rotations", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> rotationsSubBool.getValue());
    public Setting<Bones> bone = new Setting<>("Bone", Bones.Foot, this).setVisibility(v-> rotationsSubBool.getValue());
    EntityPlayer target;
    Vec3d toRotate;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> eventListener = new Listener<>(e-> {
        target = PlayerUtil.getClosestTarget(targetRange.getValue());

        //Rotations
        if (target != null) {
            switch (bone.getValue()){
                case Foot:
                    toRotate = target.getPositionVector();
                    break;
                case Legs:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1,target.getPositionVector().z);
                    break;
                case Chestplate:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.5,target.getPositionVector().z);
                    break;
                case Helmet:
                    toRotate = new Vec3d(target.getPositionVector().x,target.getPositionVector().y+1.9,target.getPositionVector().z);
                    break;
            }
        }

        if (rotate.getValue() && target != null && toRotate != null) {
            Firework.rotationManager.rotateSpoof(toRotate);
        }
    });


    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        if (target == null) return;
        RenderUtils.drawCircle(target.getPositionVector(),1, Color.CYAN,3);
    });

    public enum Bones{
        Foot, Legs, Chestplate, Helmet
    }
}

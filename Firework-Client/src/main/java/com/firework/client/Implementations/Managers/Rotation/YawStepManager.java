package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

public class YawStepManager extends Manager {

    private Minecraft mc = Minecraft.getMinecraft();

    private AnimationUtil animation;

    private YawStepRotation rotation;
    private float yawOffset;
    private float pitchOffset;

    public YawStepManager() {
        super(true);
    }

    public void post(YawStepRotation rotation){
        animation = new AnimationUtil();
        animation.setValues(1, rotation.getSpeed());
        float rotations[] = rotation.getRotations();
        yawOffset = rotations[0] - mc.player.rotationYaw;
        pitchOffset = rotations[1] - mc.player.rotationPitch;
        this.rotation = rotation;
    }

    public void finish(){
        this.rotation.getAction().accept(new Object());
        this.rotation.setResponded();
        this.rotation = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> onTick = new Listener<>(event -> {
        if(this.rotation == null) return;
        event.setCancelled(true);
        animation.update();
        RotationUtil.packetFacePitchAndYaw(mc.player.rotationPitch + pitchOffset*animation.width,
                mc.player.rotationYaw + yawOffset*animation.width);
        if(animation.width == 1)
            finish();
    });

}

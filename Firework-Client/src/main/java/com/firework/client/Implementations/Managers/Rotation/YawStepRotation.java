package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Firework;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class YawStepRotation {
    private Minecraft mc = Minecraft.getMinecraft();

    private final Vec3d rotation;
    private final float speed;
    private RotationResponse response;
    private Consumer<Object> action;
    private int priority;

    private boolean init = false;
    private float yawOffset;
    private float pitchOffset;

    public YawStepRotation(Vec3d rotation, float speed, Consumer<Object> action, int priority){
        this.rotation = rotation;
        this.speed = speed;
        this.action = action;
        this.response = new RotationResponse();
        this.priority = priority;
    }

    public float getSpeed(){
        return this.speed;
    }

    public Vec3d getRotation(){
        return this.rotation;
    }

    public int getPriority(){
        return this.priority;
    }

    public float[] getRotations(){
        return Firework.rotationManager.getRotations(this.rotation);
    }

    public Consumer<Object> getAction(){
        return this.action;
    }

    public void setResponded(){
        this.response.setResponded(true);
    }

    public RotationResponse getResponse(){
        return this.response;
    }

    public void init(){
        if(init) return;
        init = false;
        float rotations[] = getRotations();
        yawOffset = mc.player.rotationYaw - rotations[0];
        pitchOffset = mc.player.rotationPitch - rotations[1];
    }

    public float getYawOffset() {
        return yawOffset;
    }

    public float getPitchOffset() {
        return pitchOffset;
    }
}

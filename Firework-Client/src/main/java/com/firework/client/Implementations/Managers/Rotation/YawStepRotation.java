package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Firework;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class YawStepRotation {
    private final Vec3d rotation;
    private final float speed;
    private RotationResponse response;
    private final Consumer<?> action;

    public YawStepRotation(Vec3d rotation, float speed, RotationResponse response, Consumer<?> action){
        this.rotation = rotation;
        this.speed = speed;
        this.response = response;
        this.action = action;
    }

    public float getSpeed(){
        return this.speed;
    }

    public Vec3d getRotation(){
        return this.rotation;
    }

    public float[] getRotations(){
        return Firework.rotationManager.getRotations(this.rotation);
    }

    public Consumer<?> getAction(){
        return this.action;
    }

    public void setResponded(){
        this.response.setResponded(true);
    }
}

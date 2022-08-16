package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class YawStepRotation {
    private Minecraft mc = Minecraft.getMinecraft();

    private final Vec3d rotation;
    private final float speed;
    private RotationResponse response;
    private Function action;
    private int priority;

    private float yaw = 0;
    private float pitch = 0;

    public YawStepRotation(Vec3d rotation, float speed, Function action, int priority){
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

    public Function getAction(){
        return this.action;
    }

    public void setResponded(){
        this.response.setResponded(true);
    }

    public RotationResponse getResponse(){
        return this.response;
    }
}

package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Comparator;
import java.util.LinkedList;

public class YawStepManager extends Manager {

    private Minecraft mc;
    private LinkedList<YawStepRotation> rotations;

    public YawStepManager() {
        super(true);
        mc = Minecraft.getMinecraft();

        rotations = new LinkedList<>();
    }

    //Here we can post rotations
    public void post(YawStepRotation rotation){
        //Adds rotation to the queue
        rotations.add(rotation);
        //Sorts rotations by priority
        rotations.sort(Comparator.comparing(rotation1 -> -rotation1.getPriority()));
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        YawStepRotation rotation = rotations.peek();
        if(rotation == null) return;

        float rotations[] = rotation.getRotations();
        float yawOffset = rotations[0] - mc.player.rotationYaw;
        float pitchOffset = rotations[1] - mc.player.rotationPitch;

        float yaw = mc.player.rotationYaw + Math.abs(yawOffset*rotation.getSpeed());
        float pitch = mc.player.rotationPitch + Math.abs(pitchOffset*rotation.getSpeed());
        if(yaw > rotations[0])
            yaw = rotations[0];
        if(pitch > rotations[1])
            pitch = rotations[1];

        if(rotation.isInstant()){
            yaw = rotations[0];
            pitch = rotations[1];
        }


        RotationUtil.packetFacePitchAndYaw(pitch, yaw);
        event.setCancelled(true);

        if(yaw == rotations[0] && pitch == rotations[1])
            finish(rotation);
    });

    public void finish(YawStepRotation rotation){
        rotation.setResponded();
        rotation.getAction().execute();
        rotations.remove(rotation);
    }
}

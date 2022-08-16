package com.firework.client.Implementations.Managers.Rotation;

import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketPlayer;
import com.firework.client.Implementations.Utill.Render.AnimationUtil;
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

    //Rotation loop
    @Subscribe(priority = Listener.Priority.HIGHEST)
    public Listener<PacketEvent.Send> rotate = new Listener<>(event -> {
        if(!(event.getPacket() instanceof CPacketPlayer)) return;
        YawStepRotation rotation = rotations.peek();
        if(rotation == null) return;

        float rotations[] = rotation.getRotations();
        if(rotation.getYaw() == rotations[0] && rotation.getPitch() == rotations[1])
            finish(rotation);
    });

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

        rotation.setYaw(yaw);
        rotation.setPitch(pitch);
    });

    public void finish(YawStepRotation rotation){
        rotation.setResponded();
        rotation.getAction().execute();
        rotations.remove(rotation);
    }
}

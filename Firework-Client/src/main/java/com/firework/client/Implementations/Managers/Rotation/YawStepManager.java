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

    private AnimationUtil animation;
    private LinkedList<YawStepRotation> rotations;

    public YawStepManager() {
        super(true);
        mc = Minecraft.getMinecraft();

        animation = new AnimationUtil();
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
    @Subscribe
    public Listener<PacketEvent.Send> rotate = new Listener<>(event -> {
        if(!(event.getPacket() instanceof CPacketPlayer)) return;
        YawStepRotation rotation = rotations.peek();

        //Inits rotation offsets if can
        rotation.init();

        //Updates animation values
        animation.setValues(1, rotation.getSpeed());
        animation.update();

        ((ICPacketPlayer)event.getPacket()).setYaw(mc.player.rotationYaw + rotation.getYawOffset()*animation.width);
        ((ICPacketPlayer)event.getPacket()).setPitch(mc.player.rotationPitch + rotation.getPitchOffset()*animation.width);

        if(animation.width == 1)
            finish(rotation);
    });

    public void finish(YawStepRotation rotation){
        animation.width = 0;
        rotation.setResponded();
        rotations.remove(rotation);
    }
}

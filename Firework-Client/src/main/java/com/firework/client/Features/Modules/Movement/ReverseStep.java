package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "ReverseStep",
        category = Module.Category.MOVEMENT,
        description = ""
)
public class ReverseStep extends Module {


    public Setting<Integer> distance = new Setting<>("FallDistance", 3, this, 1, 5);

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
            if (!canStep()) return;

            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.75, mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.42, mc.player.posZ, mc.player.onGround));
            mc.player.setPosition(mc.player.posX, mc.player.posY-1, mc.player.posZ);
    });


    boolean canStep() {
        AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        return mc.world.getCollisionBoxes(mc.player,bb.offset(0,-1,0)).isEmpty() && !mc.player.isInLava() && !mc.player.isInWater() && !mc.player.isOnLadder() && !mc.player.movementInput.jump;
    }
}

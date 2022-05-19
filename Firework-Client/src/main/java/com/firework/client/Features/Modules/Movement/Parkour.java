package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class Parkour extends Module {
    public Parkour(){super("Parkour",Category.MOVEMENT);}
    public void tryToExecute() {
        super.tryToExecute();
        if(!mc.player.onGround || mc.gameSettings.keyBindJump.isPressed()) return;
        if(mc.player.isSneaking() || mc.gameSettings.keyBindSneak.isPressed()) return;

        AxisAlignedBB entityBoundingBox = mc.player.getEntityBoundingBox();
        AxisAlignedBB offsetBox = entityBoundingBox.offset(0, -0.5, 0).expand(-0.001, 0, -0.001);

        List<AxisAlignedBB> collisionBoxes = mc.world.getCollisionBoxes(mc.player, offsetBox);

        if(!collisionBoxes.isEmpty()) return;

        mc.player.jump();
    }
}

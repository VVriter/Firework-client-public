package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import net.minecraft.init.Blocks;

@ModuleManifest(name = "ReverseStep", category = Module.Category.MOVEMENT)
public class ReverseStep extends Module {

    boolean wasOnGround;
    boolean isStepInProgress;

    @Override
    public void onTick() {
        super.onTick();
        if(fullNullCheck()) return;

        if (this.wasOnGround && !mc.player.onGround && mc.player.motionY < 0.0D) {
            this.isStepInProgress = true;
        }

        if (this.isStepInProgress && !mc.player.onGround && (mc.world.getBlockState(mc.player.getPosition().down(2)).getBlock() != Blocks.AIR || mc.world.getBlockState(mc.player.getPosition().down(3)).getBlock() != Blocks.AIR || ReverseStep.mc.world.getBlockState(ReverseStep.mc.player.getPosition().down(4)).getBlock() != Blocks.AIR) && mc.player.motionY < 0.0D) {
            mc.player.motionY = -1.0D;
            mc.player.motionX *= 0.2D;
            mc.player.motionZ *= 0.2D;
        }

        if (this.isStepInProgress && mc.player.onGround) {
            this.isStepInProgress = false;
        }

        if (mc.player.onGround) {
            this.wasOnGround = true;
        } else {
            this.wasOnGround = false;
        }
    }
}

package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovementInput;
import net.minecraft.world.chunk.EmptyChunk;

public class EntityControl extends Module {
    public static EntityControl INSTANCE;
    public Setting<Boolean> saddle = new Setting<>("Saddle", true, this);
    public Setting<Double> speed = new Setting<>("Speed", (double)3, this, 0.5, 10);
    public Setting<Boolean> antiStuck = new Setting<>("AntiStuck", true, this);
    public EntityControl(){super("EntityControl",Category.WORLD);
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        super.onTick();
        if (Firework.minecraft.player.getRidingEntity() != null) {
            MovementInput movementInput = Firework.minecraft.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = Firework.minecraft.player.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                Firework.minecraft.player.getRidingEntity().motionX = 0.0D;
                Firework.minecraft.player.getRidingEntity().motionZ = 0.0D;
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                double sin = Math.sin(Math.toRadians(yaw + 90.0F));
                double cos = Math.cos(Math.toRadians(yaw + 90.0F));

                if (isBorderingChunk(Firework.minecraft.player.getRidingEntity(), Firework.minecraft.player.getRidingEntity().motionX, Firework.minecraft.player.getRidingEntity().motionZ)) {
                    Firework.minecraft.player.getRidingEntity().motionX = 0;
                    // TODO: Figure out if this redundant actually effects anything or is it just minecraft or my game being bullshit...
                }

                Firework.minecraft.player.getRidingEntity().motionX = (forward * speed.getValue() * cos + strafe * speed.getValue() * sin);
                Firework.minecraft.player.getRidingEntity().motionZ = (forward * speed.getValue() * sin - strafe * speed.getValue() * cos);
            }
        }
    }

    private boolean isBorderingChunk(Entity entity, double motX, double motZ) {
        return antiStuck.getValue() && Firework.minecraft.world.getChunk((int) (entity.posX + motX) >> 4, (int) (entity.posZ + motZ) >> 4) instanceof EmptyChunk;
    }

}

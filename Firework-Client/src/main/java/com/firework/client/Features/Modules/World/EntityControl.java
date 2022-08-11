package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovementInput;
import net.minecraft.world.chunk.EmptyChunk;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "EntityControl",
        category = Module.Category.WORLD,
        description = "Makes horses / pigs saddled"
)
public class EntityControl extends Module {
    public static EntityControl INSTANCE;
    public Setting<Boolean> saddle = new Setting<>("Saddle", true, this);


    public Setting<Boolean> speedSubBool = new Setting<>("Speed", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> EntitySpeed = new Setting<>("Enable", true, this).setVisibility(V-> speedSubBool.getValue());
    public Setting<Double> speed = new Setting<>("Reduction", (double)3, this, 0, 8).setVisibility(v-> EntitySpeed.getValue(true) && speedSubBool.getValue());
    public Setting<Boolean> antiStuck = new Setting<>("AntiStuck", true, this).setVisibility(v-> EntitySpeed.getValue(true) && speedSubBool.getValue());
    public EntityControl(){
        INSTANCE = this;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        super.onTick();
        if (EntitySpeed.getValue()) {
        if (mc.player.getRidingEntity() != null) {
            MovementInput movementInput = mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                mc.player.getRidingEntity().motionX = 0.0D;
                mc.player.getRidingEntity().motionZ = 0.0D;
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

                if (isBorderingChunk(mc.player.getRidingEntity(), mc.player.getRidingEntity().motionX, mc.player.getRidingEntity().motionZ)) {
                    mc.player.getRidingEntity().motionX = 0;
                    // TODO: Figure out if this redundant actually effects anything or is it just minecraft or my game being bullshit...
                }

                mc.player.getRidingEntity().motionX = (forward * speed.getValue() * cos + strafe * speed.getValue() * sin);
                mc.player.getRidingEntity().motionZ = (forward * speed.getValue() * sin - strafe * speed.getValue() * cos);
                }
            }
        }
    });

    private boolean isBorderingChunk(Entity entity, double motX, double motZ) {
        return antiStuck.getValue() && mc.world.getChunk((int) (entity.posX + motX) >> 4, (int) (entity.posZ + motZ) >> 4) instanceof EmptyChunk;
    }

}

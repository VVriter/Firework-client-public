package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

@ModuleManifest(name = "Parkour",category = Module.Category.MOVEMENT)
public class Parkour extends Module {
    public static Setting<Boolean> enabled = null;

    @Override
    public void onTick(){
        super.onTick();
        enabled = this.isEnabled;
        if(!AirJump.enabled.getValue()){
            if( mc.player.moveForward > 0 && !mc.player.collidedHorizontally){
                mc.player.setSprinting(true);
            }
            if(!mc.player.onGround || mc.gameSettings.keyBindJump.isPressed()) return;
            if(mc.player.isSneaking() || mc.gameSettings.keyBindSneak.isPressed()) return;

            AxisAlignedBB entityBoundingBox = mc.player.getEntityBoundingBox();
            AxisAlignedBB offsetBox = entityBoundingBox.offset(0, -0.5, 0).expand(-0.001, 0, -0.001);

            List<AxisAlignedBB> collisionBoxes = mc.world.getCollisionBoxes(mc.player, offsetBox);

            if(!collisionBoxes.isEmpty()) return;

            mc.player.jump();}else {
            MessageUtil.sendError("U are dumb dont use air jump with parkour helper",-1117);
        }
    }
}

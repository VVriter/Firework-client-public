package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

@ModuleManifest(name = "AimBot",category = Module.Category.COMBAT)
public class AimBot extends Module {
    @Override
    public void onTick(){
        super.onTick();
        EntityPlayer player = null;
        float tickDis = 100f;
        for (EntityPlayer p : mc.world.playerEntities) {
            if (p instanceof EntityPlayerSP) continue;
            float dis = p.getDistance(mc.player);
            if (dis < tickDis) {
                tickDis = dis;
                player = p;
            }
        }
        if (player != null) {
            Vec3d pos = EntityUtil.getInterpolatedPos(player, mc.getRenderPartialTicks());
            float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos(mc.player, mc.getRenderPartialTicks()), pos);
            //angels[1] -=  calcPitch(tickDis);
            mc.player.rotationYaw = angels[0];
            mc.player.rotationPitch = angels[1];

        }
    }
}

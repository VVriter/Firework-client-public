package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Bot extends Module {

    public Setting<Boolean> trigger = new Setting<>("Trigger", true, this);
    public Setting<Boolean> aim = new Setting<>("Aim", true, this);


    public Bot(){super("Bot",Category.COMBAT);}



    @Override
    public void tryToExecute() {
        super.tryToExecute();
        RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;

        if(trigger.getValue()){
        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {

                if (Minecraft.getMinecraft().player.getCooledAttackStrength(0) == 1) {
                    Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, objectMouseOver.entityHit);
                    Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                    Minecraft.getMinecraft().player.resetCooldown();}
                }
            }
        }
        if(aim.getValue()){
            EntityPlayer player = null;
            float tickDis = 100f;
            for (EntityPlayer p : mc.world.playerEntities) {
                if (p instanceof EntityPlayerSP ) continue;
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
}

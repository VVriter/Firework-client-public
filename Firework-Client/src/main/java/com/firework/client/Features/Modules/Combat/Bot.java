package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bot extends Module {

    public Setting<Boolean> Trigger = new Setting<>("Trigger", true, this);
    public Setting<Boolean> Aim = new Setting<>("Aim", true, this);


    public Bot(){super("Bot",Category.COMBAT);}



    @Override
    public void tryToExecute() {
        super.tryToExecute();
        RayTraceResult objectMouseOver = Firework.minecraft.objectMouseOver;

        if(Trigger.getValue()){
        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {

                if (Firework.minecraft.player.getCooledAttackStrength(0) == 1) {
                    Firework.minecraft.playerController.attackEntity(Firework.minecraft.player, objectMouseOver.entityHit);
                    Firework.minecraft.player.swingArm(EnumHand.MAIN_HAND);
                    Firework.minecraft.player.resetCooldown();}
                }
            }
        }
        if(Aim.getValue()){
            EntityPlayer player = null;
            float tickDis = 100f;
            for (EntityPlayer p : Firework.minecraft.world.playerEntities) {
                if (p instanceof EntityPlayerSP ) continue;
                float dis = p.getDistance(Firework.minecraft.player);
                if (dis < tickDis) {
                    tickDis = dis;
                    player = p;
                }
            }
            if (player != null) {
                Vec3d pos = EntityUtil.getInterpolatedPos(player, Firework.minecraft.getRenderPartialTicks());
                float[] angels = MathUtil.calcAngle(EntityUtil.getInterpolatedPos(Firework.minecraft.player, Firework.minecraft.getRenderPartialTicks()), pos);
                //angels[1] -=  calcPitch(tickDis);
                Firework.minecraft.player.rotationYaw = angels[0];
                Firework.minecraft.player.rotationPitch = angels[1];

            }
        }
    }
}

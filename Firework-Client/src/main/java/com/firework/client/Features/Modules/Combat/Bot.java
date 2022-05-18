package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Client.ClickGui;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bot extends Module {

    public Setting<Boolean> Trigger = new Setting<>("TriggerBot", true, this);
    public Setting<Boolean> Aim = new Setting<>("AimBot", true, this);
    public Setting<Boolean> BowAim = new Setting<>("BowAimBot", true, this);
    public Setting<Boolean> BowSpam = new Setting<>("BowSpamBot", true, this);
    public Setting<Double> BowSpamSpeed = new Setting<>("BowSpamSpeed", 1d, this, 1, 30);


    public Bot(){super("Bot",Category.COMBAT);}



    @Override
    public void tryToExecute() {
        super.tryToExecute();
        RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;

        if(Trigger.getValue()){
        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {

                if (Minecraft.getMinecraft().player.getCooledAttackStrength(0) == 1) {
                    Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, objectMouseOver.entityHit);
                    Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                    Minecraft.getMinecraft().player.resetCooldown();}
                }
            }
        }
        if(Aim.getValue()){
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
        if(BowAim.getValue()){
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
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
        if(BowSpam.getValue()){
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= BowSpamSpeed.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
            }
        }
    }
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@ModuleArgs(name = "BowTweaker",category = Module.Category.COMBAT)
public class BowTweaker extends Module {
    public Setting<Boolean> Aim = new Setting<>("Aim", true, this);
    public Setting<Double> BowSpamSpeed = new Setting<>("BowSpamSpeed", 17d, this, 1, 30);

    @Override
    public void onTick(){
        super.onTick();
        if(Aim.getValue()){
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
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
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= BowSpamSpeed.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
            mc.player.stopActiveHand();
        }
    }
}

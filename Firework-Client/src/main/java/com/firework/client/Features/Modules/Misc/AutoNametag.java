package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import java.util.Comparator;
import java.util.Objects;


@ModuleArgs(name = "AutoNametag",category = Module.Category.MISC)
public class AutoNametag extends Module {

    public Setting<Double> Radius = new Setting<>("Radius", (double)4, this, 0, 10);
    public Setting<Boolean> ReplaceOldNames  = new Setting<>("ReplaceOldNames", false, this);
    public Setting<Boolean> AutoSwitch  = new Setting<>("AutoSwitch", false, this);
    public Setting<Boolean> WithersOnly  = new Setting<>("WithersOnly", false, this);


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen != null)
            return;

        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemNameTag)) {
            int i1 = -1;

            if (AutoSwitch.getValue()) {
                for (int i = 0; i < 9; ++i) {
                    ItemStack item = mc.player.inventory.getStackInSlot(i);

                    if (item.isEmpty())
                        continue;

                    if (item.getItem() instanceof ItemNameTag) {
                        if (!item.hasDisplayName())
                            continue;

                        i1 = i;
                        mc.player.inventory.currentItem = i1;
                        mc.playerController.updateController();
                        break;
                    }
                }
            }

            if (i1 == -1)
                return;
        }

        ItemStack name = mc.player.getHeldItemMainhand();

        if (!name.hasDisplayName())
            return;

        EntityLivingBase l_Entity = mc.world.loadedEntityList.stream()
                .filter(p_Entity -> IsValidEntity(p_Entity, name.getDisplayName()))
                .map(p_Entity -> (EntityLivingBase) p_Entity)
                .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                .orElse(null);

        if (l_Entity != null) {

            final double[] lPos = calculateLookAt(
                    l_Entity.posX,
                    l_Entity.posY,
                    l_Entity.posZ,
                    mc.player);

           MessageUtil.sendClientMessage(String.format("Gave %s the nametag of %s", l_Entity.getName(), name.getDisplayName()),-1117);

            mc.player.rotationYawHead = (float) lPos[0];

            Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketUseEntity(l_Entity, EnumHand.MAIN_HAND));

        }
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        // to degree
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new double[]
                {yaw, pitch};
    }


    private boolean IsValidEntity(Entity entity, final String pName) {
        if (!(entity instanceof EntityLivingBase)) return false;
        if (entity.getDistance(mc.player) > Radius.getValue()) return false;
        if (entity instanceof EntityPlayer) return false;
        if (!entity.getCustomNameTag().isEmpty() && !ReplaceOldNames.getValue()) return false;

        if (ReplaceOldNames.getValue())
            if (!entity.getCustomNameTag().isEmpty() && entity.getName().equals(pName)) return false;

        return !WithersOnly.getValue() || entity instanceof EntityWither;
    }


}

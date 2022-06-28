package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

@ModuleManifest(name = "Quiver",category = Module.Category.COMBAT)
public class Quiver extends Module {


    public Setting<Enum> switchModes = new Setting<>("Switch", switchmod.Normal, this, switchmod.values());
    public enum switchmod{
        Normal, Multihand, Silent
    }



    @Override public void onEnable() {super.onEnable();
        doSwitch();
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
        mc.player.stopActiveHand();
    }

    @Override public void onTick() {super.onTick();
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, true));
    }

    @Override public void onDisable() {super.onDisable();

    }



    public void doSwitch() {
        if(switchModes.getValue(switchmod.Normal)){
            if(mc.player.inventory.hasItemStack(new ItemStack(Items.BOW))) {
            if ( (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.BOW)) ))){
                for (int j = 0; j < 9; j++) {
                    if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBow) {
                        mc.player.inventory.currentItem = j;
                        break;
                        }
                    }
                }
            }else{
                MessageUtil.sendError("You need to have ItemBow in Inventory",-1117);
                onDisable();
            }
        }else if(switchModes.getValue(switchmod.Multihand)){
            if(mc.player.inventory.hasItemStack(new ItemStack(Items.BOW))) {
            InventoryUtil.doMultiHand(Items.BOW,InventoryUtil.hands.MainHand);
            }else{
                MessageUtil.sendError("You need to have ItemBow in Inventory",-1117);
                onDisable();
            }
        }else {

        }
    }
}

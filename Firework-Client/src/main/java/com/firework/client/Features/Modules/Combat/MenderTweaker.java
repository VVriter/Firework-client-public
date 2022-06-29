package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;


@ModuleManifest(name = "MenderTweaker",category = Module.Category.COMBAT)
public class MenderTweaker extends Module {

    public Setting<Enum> switchMode = new Setting<>("Switch", switches.Silent, this, switches.values());
    public enum switches{
        Normal, Multihand, Silent, None
    }

    public Setting<Enum> mendmode = new Setting<>("Mend", mendmodes.Handly, this, mendmodes.values());
    public enum mendmodes{
        Handly, Auto, OnSneak
    }

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> armour = new Setting<>("Armour", true, this);
    public Setting<Double> threshold = new Setting<>("Threshold", (double)60, this, 1, 100).setVisibility(armour,true);
    public Setting<Boolean> disable = new Setting<>("Auto Disable", false, this);





    private boolean shouldMend = false;

    public static boolean isMending = false;

    private float prevHealth = 0.0F;


    @Override
    public void onTick(){
        super.onTick();


        //Rotates
        if(rotate.getValue() && !switchMode.getValue(switches.None)){
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));
        }else if (switchMode.getValue(switches.None) && rotate.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle){
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));
        }

        //MendMode
        if(mendmode.getValue(mendmodes.Handly) && mc.gameSettings.keyBindUseItem.isKeyDown()){
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        } else if (mendmode.getValue(mendmodes.Auto)) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        } else if(mendmode.getValue(mendmodes.OnSneak) && mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }


        //Switch
        if(switchMode.getValue(switches.Normal)){
            mc.player.inventory.currentItem = findExpInHotbar();
        } else if (switchMode.getValue(switches.Multihand)) {
            InventoryUtil.doMultiHand (findExpInHotbar()+36, InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switches.Silent)){
            mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        }





        //Armour changer
        if(armour.getValue()) {
            ItemStack[] armorStacks = new ItemStack[]{
                    mc.player.inventory.getStackInSlot(39),
                    mc.player.inventory.getStackInSlot(38),
                    mc.player.inventory.getStackInSlot(37),
                    mc.player.inventory.getStackInSlot(36)
            };

            for (int i = 0; i < 4; i++) {

                ItemStack stack = armorStacks[i];

                if (!(stack.getItem() instanceof ItemArmor)) continue;

                if (ArmorUtils.calculatePercentage(stack) < threshold.getValue()) continue;

                for (int s = 0; s < 36; s++) {

                    ItemStack emptyStack = mc.player.inventory.getStackInSlot(s);

                    if (!emptyStack.isEmpty() || !(emptyStack.getItem() == Items.AIR)) continue;

                    isMending = true;
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i + 5, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, s < 9 ? s + 36 : s, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i + 5, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.updateController();
                    return;
                }

            }

            for (int i = 0; i < 4; i++) {

                ItemStack stack = armorStacks[i];

                if (!(stack.getItem() instanceof ItemArmor)) continue;

                if (ArmorUtils.calculatePercentage(stack) >= threshold.getValue()) continue;

                shouldMend = true;
            }

            if (!shouldMend) {
                isMending = false;
                onDisable();
            }
        }
    }











    //SWitch
    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}

package com.firework.client.Features.Modules.Combat.Rewrite.MenderTweaker;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "MenderTweaker",category = Module.Category.COMBAT)
public class MenderTweaker extends Module {
    public Setting<Double> mendDelay = new Setting<>("MendDelay", (double)3, this, 1, 500);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> armour = new Setting<>("Armour", true, this);
    public Setting<Double> threshold = new Setting<>("Threshold", (double)3, this, 1, 100).setVisibility(v-> armour.getValue(true));
    public Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this).setVisibility(v-> armour.getValue(true));
    public Setting<Boolean> autoArmour = new Setting<>("AutoArmour", true, this).setVisibility(v-> armour.getValue(true));
    public Setting<Double> autoArmourDelay = new Setting<>("AutoArmourDelay", (double)3, this, 1, 500).setVisibility(v-> armour.getValue(true));

    public Setting<Enum> mendMode = new Setting<>("MendMode", mendModes.Auto, this, mendModes.values());
    public enum mendModes{
        Auto, Manual, CustomBind
    }

    public Setting<Integer> key1 = new Setting<>("CustomKey", Keyboard.KEY_NONE, this).setVisibility(v-> mendMode.getValue(mendModes.CustomBind));

    public Setting<Enum> switchMode = new Setting<>("SwitchMode", switchModes.Silent, this, switchModes.values());
    public enum switchModes{
       Normal, Multihand, Silent, None
    }

    boolean shouldMend;
    @Override
    public void onTick() {
        super.onTick();
        doSwitch();
        doRotates();
        doMend();
        doArmorChange();
        armourCheck();
        if (autoArmour.getValue() && shouldMend == false) {
            doAutoArmour();
        }
    }

    void doArmorChange() {
        if (armour.getValue() && shouldMend) {
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

                   // isMending = true;
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

            }
        }
    }

    void armourCheck() {
        //Check for empty
        int emtySlots = 0;
        if (armour.getValue()) {
            //CheckFor armour
            for (ItemStack itemStack : mc.player.inventory.armorInventory) {
                if (itemStack.getItem() == Items.AIR) {
                    emtySlots++;
                }
            }
            if (emtySlots == 4) {
               // MessageUtil.sendError("U need to be equiped in armour",-1117);
                 if (autoArmour.getValue()) {
                     shouldMend = false;
                }
            }
        }
    }

    void armourCheck2() {
        //Check for empty
        int emtySlots = 0;
        if (armour.getValue()) {
            //CheckFor armour
            for (ItemStack itemStack : mc.player.inventory.armorInventory) {
                if (itemStack.getItem() == Items.AIR) {
                    emtySlots++;
                }
            }
            if (emtySlots == 4) {
                MessageUtil.sendError("U need to be equiped in armour, or disable armour setting pls.",-1117);
                onDisable();
            }
        }
    }

    Timer mendTimer1 = new Timer();
    Timer mendTimer2 = new Timer();
    Timer mendTimer3 = new Timer();
    void doMend() {
        if (mendMode.getValue(mendModes.Auto)) {
            if (mendTimer1.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer1.reset();
            }
        } else if (mendMode.getValue(mendModes.Manual) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mendTimer2.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer2.reset();
            }
        }  else if (mendMode.getValue(mendModes.CustomBind) && Keyboard.isKeyDown(key1.getValue())) {
            if (mendTimer3.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer3.reset();
            }
        }
    }

    void doRotates() {
        if (rotate.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));
        }
    }

    void doSwitch() {
        if (switchMode.getValue(switchModes.Normal)) {
            mc.player.inventory.currentItem = MenderInventoryHelper.findExpInHotbar();
        } else if (switchMode.getValue(switchModes.Multihand)) {
            InventoryUtil.doMultiHand (MenderInventoryHelper.findExpInHotbar()+36, InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switchModes.Silent)) {
            if (mendMode.getValue(mendModes.Manual) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            } else if (mendMode.getValue(mendModes.Auto)) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            } else if (mendMode.getValue(mendModes.CustomBind) && Keyboard.isKeyDown(key1.getValue())) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            }
        } else if (switchMode.getValue(switchModes.None)) {
            //Do nothing
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
        shouldMend = true;
        armourCheck2();
        autoArmourTimer.reset();
        mendTimer1.reset();
        mendTimer2.reset();
        mendTimer3.reset();
    }


    Timer autoArmourTimer = new Timer();
    void doAutoArmour() {
        if(autoArmour.getValue()) {
        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];

        for (int armourType = 0; armourType < 4; armourType++) {
            ItemStack oldArmour = mc.player.inventory.armorItemInSlot(armourType);
            if (oldArmour.getItem() instanceof ItemArmor) {
                bestArmorValues[armourType] = (((ItemArmor) oldArmour.getItem()).damageReduceAmount);
            }
            bestArmorSlots[armourType] = -1;
        }

        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(slot);

            if (stack.getCount() > 1)
                continue;

            if(!(stack.getItem() instanceof ItemArmor))
                continue;

            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorType = armor.armorType.ordinal() - 2;

            if (armorType == 2 && mc.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA)) continue;

            int armorValue = armor.damageReduceAmount;

            if(armorValue > bestArmorValues[armorType])
            {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }

        for (int armorType = 0; armorType < 4; armorType++) {
            // check if better armor was found
            int slot = bestArmorSlots[armorType];
            if(slot == -1)
                continue;

            // check if armor can be swapped
            // needs 1 free slot where it can put the old armor
            ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
            if(oldArmor != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1)
            {
                // hotbar fix
                if(slot < 9)
                    slot += 36;

                // swap armor
                if (autoArmourTimer.hasPassedMs(autoArmourDelay.getValue())) {
                mc.playerController.windowClick(0, 8 - armorType, 0,
                        ClickType.QUICK_MOVE, mc.player);
                mc.playerController.windowClick(0, slot, 0,
                        ClickType.QUICK_MOVE, mc.player);
                autoArmourTimer.reset();

                break;
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        autoArmourTimer.reset();
        mendTimer1.reset();
        mendTimer2.reset();
        mendTimer3.reset();
    }
}
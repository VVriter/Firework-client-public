package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

@ModuleManifest(name = "AutoMend",category = Module.Category.COMBAT)
public class AutoMend extends Module {

    public Setting<Double> delay = new Setting<>("Threshold", (double)80, this, 0, 100);

    public Setting<Enum> mode = new Setting<>("Mode", modes.Stupid, this, modes.values());
    public enum modes{
        Stupid, Smart
    }

    public Setting<Enum> switchMode = new Setting<>("Switch", switches.Silent, this, switches.values());
    public enum switches{
        Normal, Multihand, Silent
    }


    public Setting<Boolean> armorChanger = new Setting<>("Armor Change", true, this).setVisibility(mode,modes.Smart);
   // public Setting<Boolean> elythraToo = new Setting<>("Elytra Too", false, this).setVisibility(armorChanger,true);



    public Setting<Boolean> onSneak = new Setting<>("On Sneak", true, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);

    public Setting<Boolean> autoDisable = new Setting<>("Auto Disable", true, this).setVisibility(mode,modes.Smart);


    private boolean shouldMend = false;
    public static boolean isMending = false;

  @Override
  public void onTick(){
        super.onTick();

        if(!onSneak.getValue() && mode.getValue(modes.Stupid)){
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }

        if(onSneak.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()){
            doSwitch();
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }




        if(mode.getValue(modes.Smart)){

            ItemStack[] armorStacks = new ItemStack[]{
                    mc.player.inventory.getStackInSlot(39),
                    mc.player.inventory.getStackInSlot(38),
                    mc.player.inventory.getStackInSlot(37),
                    mc.player.inventory.getStackInSlot(36)
            };
            for (int i = 0; i < 4; i++) {
                ItemStack stack = armorStacks[i];
                if (!(stack.getItem() instanceof ItemArmor)) {onDisable();};}


            doSwitch();
            if(armorChanger.getValue()) {
                changeArmor();
            }
            if(shouldMend){
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }

  }






    //Armor
    private void changeArmor() {
        ItemStack[] armorStacks = new ItemStack[]{
                mc.player.inventory.getStackInSlot(39),
                mc.player.inventory.getStackInSlot(38),
                mc.player.inventory.getStackInSlot(37),
                mc.player.inventory.getStackInSlot(36)
        };
        for (int i = 0; i < 4; i++) {

            ItemStack stack = armorStacks[i];


            if (!(stack.getItem() instanceof ItemArmor)) continue;

            if (ArmorUtils.calculatePercentage(stack) < delay.getValue().intValue()) continue;

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

            if (ArmorUtils.calculatePercentage(stack) >= delay.getValue().intValue()) continue;

            shouldMend = true;
        }

        if (!shouldMend) {
            isMending = false;
            onDisable();
        }
    }






    //Silent switch
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


    private void doSwitch(){
        if(rotate.getValue()){
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));}
      if(switchMode.getValue(switches.Normal)){
          mc.player.inventory.currentItem = findExpInHotbar();
      } else if (switchMode.getValue(switches.Multihand)) {
          InventoryUtil.doMultiHand (findExpInHotbar()+36, InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switches.Silent)){
          mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
      }
    }
    
    
    
    
    @Override
    public void onDisable(){
      super.onDisable();
        if (mc.player.ticksExisted % 2 == 0) {
            return;
        }
        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final int[] bestArmorSlots = new int[4];
        final int[] bestArmorValues = new int[4];
        for (int armorType = 0; armorType < 4; ++armorType) {
            final ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
            if (oldArmor != null && oldArmor.getItem() instanceof ItemArmor) {
                bestArmorValues[armorType] = ((ItemArmor)oldArmor.getItem()).damageReduceAmount;
            }
            bestArmorSlots[armorType] = -1;
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(slot);
            if (stack.getCount() <= 1) {
                if (stack != null) {
                    if (stack.getItem() instanceof ItemArmor) {
                        final ItemArmor armor = (ItemArmor)stack.getItem();
                        final int armorType2 = armor.armorType.ordinal() - 2;
                        if (armorType2 != 2 || !mc.player.inventory.armorItemInSlot(armorType2).getItem().equals(Items.ELYTRA)) {
                            final int armorValue = armor.damageReduceAmount;
                            if (armorValue > bestArmorValues[armorType2]) {
                                bestArmorSlots[armorType2] = slot;
                                bestArmorValues[armorType2] = armorValue;
                            }
                        }
                    }
                }
            }
        }
        for (int armorType = 0; armorType < 4; ++armorType) {
            int slot2 = bestArmorSlots[armorType];
            if (slot2 != -1) {
                final ItemStack oldArmor2 = mc.player.inventory.armorItemInSlot(armorType);
                if (oldArmor2 == null || oldArmor2 != ItemStack.EMPTY || mc.player.inventory.getFirstEmptyStack() != -1) {
                    if (slot2 < 9) {
                        slot2 += 36;
                    }
                    mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
                    mc.playerController.windowClick(0, slot2, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
                    break;
                }
            }
        }
    }
}

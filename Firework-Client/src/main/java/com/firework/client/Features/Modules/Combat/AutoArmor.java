package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "AutoArmor", category = Module.Category.COMBAT)
public class AutoArmor extends Module {

    public Setting<Integer> delay = new Setting<>("Delay", 3, this, 0, 10);

    public Setting<Boolean> elytraReplace = new Setting<>("ElytraReplace", true, this);

    public int remainingDelay;

    @Override
    public void onEnable() {
        super.onEnable();
        remainingDelay = 0;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
       if(fullNullCheck() || mc.player.ticksExisted < 4) return;

       if(remainingDelay > 0)
           remainingDelay--;

       if(remainingDelay != 0) return;
       remainingDelay = delay.getValue();

        if (mc.currentScreen instanceof GuiInventory) return;

        for(EntityEquipmentSlot type : EntityEquipmentSlot.values()){
            int slot = findArmorSlot(type);
            if(slot == -1) continue;
            ItemStack itemStack = InventoryUtil.getItemStack(InventoryUtil.getSlotFromEquipment(type));
            if(itemStack == null) continue;
            if(itemStack.isEmpty()) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, InventoryUtil.getClickSlot(slot), 0, ClickType.QUICK_MOVE, mc.player);
                break;
            }else if(elytraReplace.getValue() && itemStack.getItem() instanceof ItemElytra){
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, InventoryUtil.getClickSlot(slot), 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, InventoryUtil.getSlotFromEquipment(type), 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, InventoryUtil.getClickSlot(slot), 0, ClickType.PICKUP, mc.player);
                break;
            }
        }

    });

    public int findArmorSlot(EntityEquipmentSlot type) {
        int slot = -1;
        int value = 0;

        for(int i = 0; i < mc.player.inventory.mainInventory.size(); i++){
            ItemStack itemStack = InventoryUtil.getItemStack(i);
            if(!(itemStack.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = ((ItemArmor) itemStack.getItem());
            if(armor.getEquipmentSlot() != type) continue;

            if(value < getArmorValue(itemStack)) {
                value = getArmorValue(itemStack);
                slot = i;
            }
        }
        return slot;
    }

    public int getArmorValue(ItemStack stack) {
        ItemArmor item = ((ItemArmor)stack.getItem());
        int armorPoints = item.damageReduceAmount;
        int armorToughness = (int) item.toughness;
        int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        Enchantment protection = Enchantments.BLAST_PROTECTION;
        int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        DamageSource dmgSource = DamageSource.causePlayerDamage(mc.player);
        int prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }

}

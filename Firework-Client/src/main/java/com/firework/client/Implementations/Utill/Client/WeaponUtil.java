package com.firework.client.Implementations.Utill.Client;

import com.firework.client.Implementations.Mixins.MixinsList.IItemTool;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

import static com.firework.client.Implementations.Utill.Util.mc;

public class WeaponUtil {

    public static int theMostPoweredWeapon(Entity target){
        Pair<Integer, Float> theMostPowered = null;
        for(int i = 0; i <= 35; i++){
            ItemStack stack = InventoryUtil.getItemStack(i);
            if(stack.getItem() instanceof ItemSword){
                if(theMostPowered == null) {
                    theMostPowered = new Pair(i, ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage());
                }else if((float)theMostPowered.two < ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage()){
                    theMostPowered = new Pair(i, ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage());
                }
            }
            if (stack.getItem() instanceof ItemSword) {
                float damage = ((ItemSword) stack.getItem()).getAttackDamage() + EnchantmentHelper.getModifierForCreature(stack, target instanceof EntityLivingBase ? ((EntityLivingBase) target).getCreatureAttribute() : EnumCreatureAttribute.UNDEFINED);
                if(theMostPowered == null) {
                    theMostPowered = new Pair(i, damage);
                }else if(theMostPowered.two < damage){
                    theMostPowered = new Pair(i, damage);
                }
            } else if (stack.getItem() instanceof ItemTool) {
                float damage = ((IItemTool) stack.getItem()).getAttackDamage() + EnchantmentHelper.getModifierForCreature(stack, target instanceof EntityLivingBase ? ((EntityLivingBase) target).getCreatureAttribute() : EnumCreatureAttribute.UNDEFINED);
                if(theMostPowered == null) {
                    theMostPowered = new Pair(i, damage);
                }else if(theMostPowered.two < damage){
                    theMostPowered = new Pair(i, damage);
                }
            }
        }
        return (int) theMostPowered.one;
    }

    public int findBestWeapon(Entity target) {
        int bestSlot = -1;
        float bestDamage = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || stack.getItem() == Items.AIR) continue;

        }
        return bestSlot;
    }
}

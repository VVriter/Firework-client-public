package com.firework.client.Implementations.Utill.Client;

import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class WeaponUtil {

    public static int theMostPoweredSword(){
        Pair theMostPowered = null;
        for(int i = 0; i <= 35; i++){
            if(InventoryUtil.getItemStack(i).getItem() instanceof ItemSword){
                if(theMostPowered == null) {
                    theMostPowered = new Pair(i, ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage());
                }else if((float)theMostPowered.two < ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage()){
                    theMostPowered = new Pair(i, ((ItemSword) InventoryUtil.getItemStack(i).getItem()).getAttackDamage());
                }
            }
        }
        return (int) theMostPowered.one;
    }
}

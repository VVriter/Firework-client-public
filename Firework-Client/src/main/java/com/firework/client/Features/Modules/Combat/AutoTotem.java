package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;

import java.util.Arrays;

@ModuleArgs(name = "AutoTotem",category = Module.Category.COMBAT)
public class AutoTotem extends Module {
    public Setting<String> hand = new Setting<>("Mode", "MainHand", this, Arrays.asList("MainHand", "OffHand"));

    @Override
    public void onTick(){
        super.onTick();



        if(hand.getValue().equals("MainHand")){
            makeNormalSwitch();
        }else if(hand.getValue().equals("OffHand")){

        }
    }












    public void makeNormalSwitch(){
        if ( (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.TOTEM_OF_UNDYING)) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem().equals(Items.TOTEM_OF_UNDYING)) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }
    }

}

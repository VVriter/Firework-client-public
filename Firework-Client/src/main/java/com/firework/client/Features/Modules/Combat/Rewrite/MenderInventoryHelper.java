package com.firework.client.Features.Modules.Combat.Rewrite;

import net.minecraft.init.Items;

import static com.firework.client.Features.Modules.Module.mc;

public class MenderInventoryHelper {
    public static int findExpInHotbar() {
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

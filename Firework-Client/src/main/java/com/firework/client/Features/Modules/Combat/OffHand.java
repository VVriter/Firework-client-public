package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;


@ModuleArgs(name = "OffHand",category = Module.Category.COMBAT)
public class OffHand extends Module {
    public Setting<Enum> offhandMode = new Setting<>("Offhand", modes.Totem, this, modes.values());
    public Setting<totemModes> hand = new Setting<>("Hand", totemModes.MainHand, this, totemModes.values());

    @Override
    public void onTick(){
        super.onTick();
        if(offhandMode.getValue(modes.Totem)) {
            doOffHand(Items.TOTEM_OF_UNDYING);
        } else if(offhandMode.getValue(modes.Crystal))
            doOffHand(Items.END_CRYSTAL);
    }

    public void swapItems(int slot, int step) {
        if (step == 0) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        if (step == 1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 36 + mc.player.inventory.currentItem, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        for(int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == input) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    public void doOffHand(Item item){
        if (hand.getValue(totemModes.MainHand)) {
            if(mc.player.getHeldItemMainhand().getItem() != item)
                swapItems(getItemSlot(item),1);
        } else if (hand.getValue(totemModes.OffHand)) {
            if(mc.player.getHeldItemOffhand().getItem() != item)
                swapItems(getItemSlot(item),0);
        }
    }
    public enum totemModes {
        MainHand, OffHand
    }

    public enum modes {
        Totem, Crystal
    }
}

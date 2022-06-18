package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static com.firework.client.Implementations.Utill.InventoryUtil.*;


@ModuleArgs(name = "MultiHand",category = Module.Category.COMBAT)
public class MultiHand extends Module {
    public Setting<Enum> multiHandMode = new Setting<>("MultiHand", modes.Totem, this, modes.values());
    public Setting<hands> totemHandMode = new Setting<>("Hand", hands.MainHand, this, hands.values()).setVisibility(multiHandMode, modes.Totem);
    public Setting<hands> crystalHandMode = new Setting<>("Hand", hands.MainHand, this, hands.values()).setVisibility(multiHandMode, modes.Crystal);

    public Setting<Boolean> parallel = new Setting<>("Parallel", false, this);
    @Override
    public void onTick(){
        super.onTick();
        if(!parallel.getValue()) {
            if (multiHandMode.getValue(modes.Totem))
                doOffHand(Items.TOTEM_OF_UNDYING, totemHandMode);
            else if (multiHandMode.getValue(modes.Crystal))
                doOffHand(Items.END_CRYSTAL, crystalHandMode);
        }else {
            doOffHand(Items.TOTEM_OF_UNDYING, totemHandMode);
            doOffHand(Items.END_CRYSTAL, crystalHandMode);
        }
    }

    public void doOffHand(Item item, Setting<hands> hand){
        if (hand.getValue(hands.MainHand)) {
            if (mc.player.getHeldItemMainhand().getItem() != item)
                swapItems(getItemSlot(item), 1);
        } else if (hand.getValue(hands.OffHand)) {
            if (mc.player.getHeldItemOffhand().getItem() != item)
                swapItems(getItemSlot(item), 0);
        }
    }
    public enum hands{
        MainHand, OffHand
    }

    public enum modes {
        Totem, Crystal
    }
}

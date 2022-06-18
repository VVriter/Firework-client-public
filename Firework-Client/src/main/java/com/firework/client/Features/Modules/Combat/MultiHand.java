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
    public Setting<hands> totemHandMode = new Setting<>("Totem", hands.MainHand, this, hands.values()).setVisibility(multiHandMode, modes.Totem);
    public Setting<hands> crystalHandMode = new Setting<>("Crystal", hands.MainHand, this, hands.values()).setVisibility(multiHandMode, modes.Crystal);

    public Setting<Boolean> parallel = new Setting<>("Parallel", false, this);
    @Override
    public void onTick(){
        super.onTick();
        if(!parallel.getValue()) {
            if (multiHandMode.getValue(modes.Totem))
                doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode);
            else if (multiHandMode.getValue(modes.Crystal))
                doMultiHand(Items.END_CRYSTAL, crystalHandMode);
        }else {
            doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode);
            doMultiHand(Items.END_CRYSTAL, crystalHandMode);
        }
    }

    public enum modes {
        Totem, Crystal
    }
}

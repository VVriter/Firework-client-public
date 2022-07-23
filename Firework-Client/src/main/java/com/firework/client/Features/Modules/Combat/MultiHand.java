package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;

import static com.firework.client.Implementations.Utill.InventoryUtil.doMultiHand;
import static com.firework.client.Implementations.Utill.InventoryUtil.hands;


@ModuleManifest(name = "MultiHand",category = Module.Category.COMBAT)
public class MultiHand extends Module {
    public Setting<Enum> multiHandMode = new Setting<>("MultiHand", modes.Totem, this);
    public Setting<hands> totemHandMode = new Setting<>("Totem", hands.MainHand, this).setVisibility(v-> multiHandMode.getValue(modes.Totem));
    public Setting<hands> crystalHandMode = new Setting<>("Crystal", hands.MainHand, this).setVisibility(v-> multiHandMode.getValue(modes.Crystal));

    public Setting<Boolean> parallel = new Setting<>("Parallel", false, this);
    @Override
    public void onTick(){
        super.onTick();
        if(!parallel.getValue()) {
            if (multiHandMode.getValue(modes.Totem))
                doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode.getValue());
            else if (multiHandMode.getValue(modes.Crystal))
                doMultiHand(Items.END_CRYSTAL, crystalHandMode.getValue());
        }else {
            doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode.getValue());
            doMultiHand(Items.END_CRYSTAL, crystalHandMode.getValue());
        }
    }

    public enum modes {
        Totem, Crystal
    }
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import static com.firework.client.Implementations.Utill.InventoryUtil.doMultiHand;
import static com.firework.client.Implementations.Utill.InventoryUtil.hands;


@ModuleManifest(
        name = "MultiHand",
        category = Module.Category.COMBAT,
        description = "AutoTotem lul"
)
public class MultiHand extends Module {
    public Setting<Enum> multiHandMode = new Setting<>("MultiHand", modes.Totem, this);
    public Setting<hands> totemHandMode = new Setting<>("Totem", hands.MainHand, this).setVisibility(v-> multiHandMode.getValue(modes.Totem));
    public Setting<hands> crystalHandMode = new Setting<>("Crystal", hands.MainHand, this).setVisibility(v-> multiHandMode.getValue(modes.Crystal));

    public Setting<Boolean> parallel = new Setting<>("Parallel", false, this);
    public Setting<Boolean> packetSpoof = new Setting<>("PacketSpoof", true, this);

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        if(!parallel.getValue()) {
            if (multiHandMode.getValue(modes.Totem))
                doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode.getValue(), packetSpoof.getValue());
            else if (multiHandMode.getValue(modes.Crystal))
                doMultiHand(Items.END_CRYSTAL, crystalHandMode.getValue(), packetSpoof.getValue());
        }else {
            doMultiHand(Items.TOTEM_OF_UNDYING, totemHandMode.getValue(), packetSpoof.getValue());
            doMultiHand(Items.END_CRYSTAL, crystalHandMode.getValue(), packetSpoof.getValue());
        }
    });

    public enum modes {
        Totem, Crystal
    }
}

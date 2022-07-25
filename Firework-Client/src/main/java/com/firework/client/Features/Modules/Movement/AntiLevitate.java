package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.potion.Potion;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Arrays;
import java.util.Objects;

@ModuleManifest(name = "AntiLevitate",category = Module.Category.MOVEMENT)
public class AntiLevitate extends Module {
    public Setting<String> setting = new Setting<>("Mode", "EffectRemove", this, Arrays.asList("EffectRemove", "Fly"));

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(setting.getValue().equals("EffectRemove")){
            if (mc.player.isPotionActive((Potion) Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
                mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
            }else{
                if(mc.player.isPotionActive((Potion) Objects.requireNonNull(Potion.getPotionFromResourceLocation("Fly")))){
                    Fly.enabled.setValue(true);
                }
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        Fly.enabled.setValue(false);
    }
}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class SlowAnimations extends Module {
    private static Minecraft mc = Minecraft.getMinecraft();

    public SlowAnimations(){super("SlowAnimations",Category.RENDER);}

    public void onEnable() {
        SlowAnimations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000));
        }

    public void onDisable() {
        SlowAnimations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
    }
}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "FullBright",category = Module.Category.RENDER)
public class FullBright extends Module {

    public Setting<Enum> enumSetting = new Setting<>("Mode", TestEnum.Gamma, this, TestEnum.values());
    public enum TestEnum{
        Gamma, Potion, CustomTime
    }

    public Setting<Double> time = new Setting<>("CustomTime", (double)3, this, 1, 23000).setVisibility(enumSetting,TestEnum.CustomTime);
    @Override
    public void onEnable() {
        super.onEnable();
        if (enumSetting.getValue(TestEnum.Gamma)) {
            new Thread(() -> {
                try {
                    for (int i = 1; i < 101; ) {
                        try {
                            Thread.sleep(50);
                            mc.gameSettings.gammaSetting = i;
                            i++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }).start();
        } else if (enumSetting.getValue(TestEnum.Potion)) {
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
        } else if (enumSetting.getValue(TestEnum.CustomTime)) {
            mc.world.setWorldTime(time.getValue().longValue());
        }

    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if(enumSetting.getValue(TestEnum.CustomTime)){
            event.setCanceled(true);
            }
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        mc.gameSettings.gammaSetting = 0;
    }
}

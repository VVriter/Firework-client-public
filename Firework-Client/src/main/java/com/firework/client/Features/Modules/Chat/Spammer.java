package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class Spammer extends Module {
    public Spammer(){ super("Spammer", Category.CHAT); }

    private int delay = -1;
    private String message;

    public Setting<Double> delaySeconds = new Setting<>("Delay (in seconds)", 0.3, this, 0.3, 10);
    public Setting<Boolean> antiFilter = new Setting<>("AntiFilter", false, this);

    @Override
    public void onEnable() {
        super.onEnable();

        //TODO: Load message from json
        message = "TEST!";
    }

    @Override
    public void onTick() {
        System.out.println(delay);
        delay++;
        if(delay >= 20 * delaySeconds.getValue()) {
            System.out.println("!");
            String reformattedMessage = message;
            Random random = new Random();

            if(antiFilter.getValue()) {
                reformattedMessage += " | " + random.nextInt(2048);
            }

            Minecraft.getMinecraft().player.sendChatMessage(reformattedMessage);

            delay = -1;
        }
    }
}

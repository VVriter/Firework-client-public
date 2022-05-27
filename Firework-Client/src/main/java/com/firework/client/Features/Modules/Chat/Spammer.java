package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Spammer extends Module {
    public Spammer(){ super("Spammer", Category.CHAT); }

    private int delay = -1;
    public static String message = "Spamm!";

    public Setting<Double> delaySeconds = new Setting<>("Delay (in seconds)", 0.5, this, 0.3, 10);
    public Setting<Boolean> antiFilter = new Setting<>("AntiFilter", false, this);

    @Override
    public void onEnable() {
        super.onEnable();

        JSONObject obj = new JSONObject();
        obj.put("Text", "Spamm!");

        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Spam.json");
        if (!theDir.exists()){
            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Spam.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTick() {
        if(!Display.isActive()) return;
        new Thread(() -> spam()).run();
    }

    private void spam() {
        delay++;
        if(delay >= 20 * delaySeconds.getValue()) {
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

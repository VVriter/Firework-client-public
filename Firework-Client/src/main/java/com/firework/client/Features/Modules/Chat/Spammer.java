package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Parser.JsonReader;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.compress.utils.Charsets;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.Display;

import java.io.*;
import java.util.Random;

public class Spammer extends Module {
    public Spammer(){ super("Spammer", Category.CHAT); }

    private int delay = -1;
    public static String message = "Spamm!";

    public Setting<Double> delaySeconds = new Setting<>("Delay (in seconds)", 1.3, this, 1.3, 10);
    public Setting<Boolean> antiFilter = new Setting<>("AntiFilter", false, this);

    public Setting<Boolean> autoWhisperer = new Setting<>("AutoWhisperer", false, this);

    @Override
    public void onEnable() {
        super.onEnable();

        JSONObject obj = new JSONObject();
        obj.put("Text", "Spamm!");

        try {
            File file = new File(Firework.FIREWORK_DIRECTORY+"Spam.json");

            if (file.exists()){
                return;
            }

            FileOutputStream fileStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fileStream, Charsets.UTF_16);
            writer.write(obj.toJSONString());
            writer.close();
        } catch (Exception e) {
            //NOTHING...........
        }

        JsonReader.getSpamText();
    }

    @Override
    public void onTick() {
        if(!Display.isActive()) return;
        //new Thread(() -> spam()).run();
        spam();
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

            if(autoWhisperer.getValue()) {
                for (NetworkPlayerInfo player : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
                    String name = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(player);
                    String command = "/w " + name + " " + reformattedMessage;

                    Minecraft.getMinecraft().player.sendChatMessage(command);
                }
            }

            delay = -1;
        }
    }
}

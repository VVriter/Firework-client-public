package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Parser.JsonReader;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Spammer extends Module {
    public Spammer(){ super("Spammer", Category.CHAT); }

    private int delay = -1;
    public static String message = "Spam!";

    public Setting<Double> delaySeconds = new Setting<>("Delay (in seconds)", 1.3, this, 1.3, 10);
    public Setting<Boolean> antiFilter = new Setting<>("AntiFilter", false, this);
    public Setting<Boolean> autoWhisperer = new Setting<>("Whisperer", false, this);

    @Override
    public void onEnable() {
        super.onEnable();

        JSONObject obj = new JSONObject();
        obj.put("Text", "Spam!");

        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Spam.json");
        if (!theDir.exists()){
            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Spam.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            String[] messageArray = message.split("-");
            String reformattedMessage = messageArray[new Random().nextInt(messageArray.length)];
            Random random = new Random();

            if(antiFilter.getValue()) {
                reformattedMessage += " | " + random.nextInt(2048);
            }

            if(!autoWhisperer.getValue()) {
                Minecraft.getMinecraft().player.sendChatMessage(reformattedMessage);
            }
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

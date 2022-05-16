package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Parser.JsonReader;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.event.ClickEvent;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@CommandManifest(label = "prefix")
public class PrefixCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Creates new JSon object
        JSONObject obj = new JSONObject();
        obj.put("Prefix", args[1]);


            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Prefix.json")) {
                file.write(obj.toJSONString());
                JsonReader.getPrefix();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

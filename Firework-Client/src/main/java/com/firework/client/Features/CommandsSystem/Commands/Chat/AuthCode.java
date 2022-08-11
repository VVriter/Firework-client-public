package com.firework.client.Features.CommandsSystem.Commands.Chat;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Chat.AutoAuth;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import sun.misc.MessageUtils;

import java.io.FileWriter;
import java.io.IOException;

@CommandManifest(label = "auth")
public class AuthCode extends Command {
    @Override
    public void execute(String[] args) {
        //Creates new JSon object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject obj = new JsonObject();
        obj.addProperty("auth", args[1]);


        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "AutoAuth.json")) {
            gson.toJson(obj, file);
            AutoAuth.authCode = args[1];
            MessageUtil.sendClientMessage("Setted AutoAuth password!",-1117);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

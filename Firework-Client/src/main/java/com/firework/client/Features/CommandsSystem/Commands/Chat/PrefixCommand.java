package com.firework.client.Features.CommandsSystem.Commands.Chat;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;


@CommandManifest(label = "prefix")
public class PrefixCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Creates new JSon object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject obj = new JsonObject();
        obj.addProperty("Prefix", args[1]);


            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Prefix.json")) {
                gson.toJson(obj, file);
                CommandManager.prefix = args[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

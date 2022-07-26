package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@CommandManifest(label = "webhook")
public class WebhookCommand extends Command {
    @Override
    public void execute(String[] args) {
        try {
            addWebhook(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            MessageUtil.sendError("Usage - "+ CommandManager.prefix + "webhook <webhook link>",-1117);
        }
    }

    void addWebhook(String webhook) {
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Webhook");
        if (!theDir.exists()){
            theDir.mkdirs();}

        JsonObject obj = new JsonObject();
        obj.addProperty("webhook",webhook);
        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Webhook/" + "Webhook" + ".json")) {
            MessageUtil.sendClientMessage("Webhook is linked, check discord channel.", -1117);
            DiscordUtil.sendMsg("```Webhook is already linked```", webhook);
            DiscordNotificator.webhook = webhook;
            new Gson().toJson(obj ,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

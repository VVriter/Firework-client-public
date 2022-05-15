package com.firework.client.Features.CommandsSystem.Commands;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Managers.Parser.JsonParser;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;

@CommandManifest(label = "webhook")
public class WebhookCommand extends Command {

    @Override
    public void execute(String[] args) {
        try {
            if (!args[1].matches("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
                MessageUtil.sendClientMessage("kinda seems to be not a webhook bro", false);
                return;
            }
            JsonParser.DISORD_WEBHOOK = args[1];
            JsonParser.createWebhookJsonFile();
            MessageUtil.sendClientMessage("Webhook changed", false);
            DiscordUtil.sendMsg("```Ur webhook is already linked```",args[1]);
        } catch (Exception e) {
            MessageUtil.sendClientMessage("Something went wrong.", false);
            e.printStackTrace();
        }
    }
}
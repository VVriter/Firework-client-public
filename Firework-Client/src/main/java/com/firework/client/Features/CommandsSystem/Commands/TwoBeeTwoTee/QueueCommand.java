package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util.ApiRequester;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "queue")
public class QueueCommand extends Command {
    @Override
    public void execute(String[] args) {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
        MessageUtil.sendClientMessage("Players in queue: "+ApiRequester.getPrioQueueLength(),false);

        /*String text = NetworkUtil.makeRequest("https://2bqueue.info/queue");
                            if (text == null || text.isEmpty() || text.equals("[]")) {
                                MessageUtil.sendError("An exception occurred when retrieving the date, or this player has not been seen.",-1117);
                            }
                            JsonArray array = new Gson().fromJson(text, JsonArray.class);
                            if (array.size() == 0) {
                                MessageUtil.sendError("This player has not been seen.",-1117);
                            }
                            JsonObject data = array.get(0).getAsJsonObject();


        MessageUtil.sendClientMessage("Players in normal queue "+data.get("regular"),false);*/

                        }catch (Exception e){

                        }
                    }
                }).start();
    }
}

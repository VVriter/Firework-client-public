package com.firework.client.Implementations.Managers.Parser;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {
    public static void parse() {
        Desktop desktop = Desktop.getDesktop();

        //Creates new JSon object
        JSONObject obj = new JSONObject();
        obj.put("Firework", "bebra");


        //Creates dir if it really needed
        File theDir = new File(Firework.FIREWORK_DIRECTORY);
        if (!theDir.exists()){
            theDir.mkdirs();



            //If this is first start, open link with advertising
            DiscordUtil.OpenServer();
            //Creates main Json file
            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Firework.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //For WebhookCommand
    public static String DISORD_WEBHOOK = "";
    public static void createWebhookJsonFile(){

       // File theWebhookJsonFileDir = new File(Firework.FIREWORK_DIRECTORY+"Webhook.json");


        //Creates new JSon object
        JSONObject web = new JSONObject();
        web.put("Webhook", DISORD_WEBHOOK);

        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Webhook.json")) {
            file.write(web.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

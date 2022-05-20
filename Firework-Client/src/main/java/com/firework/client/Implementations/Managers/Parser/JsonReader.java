package com.firework.client.Implementations.Managers.Parser;

import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.CommandManager.CommandManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class JsonReader {

    public static void getPrefix() {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\Prefix.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String prefix = (String) jsonObject.get("Prefix");
            System.out.println(prefix);
            CommandManager.prefix = prefix;



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void getWebhook(){
        JSONParser parser = new JSONParser();

        try (Reader reader1 = new FileReader(System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\Webhook.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader1);
            System.out.println(jsonObject);

            String webhook = (String) jsonObject.get("Webhook");
            System.out.println(webhook);
            DiscordNotificator.webhook = webhook;



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
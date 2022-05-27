package com.firework.client.Implementations.Managers.Parser;

import com.firework.client.Features.Modules.Chat.Spammer;
import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Firework;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonReader {

    public static void getPrefix() {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(Firework.FIREWORK_DIRECTORY + "\\Prefix.json")) {

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

    public static void getSpamText() {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(Firework.FIREWORK_DIRECTORY + "\\Spam.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String text = (String) jsonObject.get("Text");
            System.out.println(text);
            Spammer.message = text;



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void getWebhook(){
        JSONParser parser = new JSONParser();

        try (Reader reader1 = new FileReader(Firework.FIREWORK_DIRECTORY + "\\Webhook.json")) {

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
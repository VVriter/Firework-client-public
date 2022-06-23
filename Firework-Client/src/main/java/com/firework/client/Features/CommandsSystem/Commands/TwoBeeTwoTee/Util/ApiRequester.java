package com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.Util;

import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;

public class ApiRequester {

    private static SimpleEntry entry = new SimpleEntry(Integer.valueOf(0), Long.valueOf(System.currentTimeMillis()));
    private static SimpleEntry entry2 = new SimpleEntry("0m", Long.valueOf(System.currentTimeMillis()));

    public static int getPrioQueueLength() {
        if (((Integer) ApiRequester.entry.getKey()).intValue() != 0 && System.currentTimeMillis() - ((Long) ApiRequester.entry.getValue()).longValue() <= 30000L) {
            return ((Integer) ApiRequester.entry.getKey()).intValue();
        } else {
            JsonParser jsonParser = new JsonParser();
            String url = "https://api.2b2t.dev/prioq";

            try {
                HttpURLConnection e = (HttpURLConnection) (new URL("https://api.2b2t.dev/prioq")).openConnection();

                e.setRequestProperty("User-Agent", "Sanku-Bot/1.0.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(e.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();

                in.lines().forEach(result::append);
                in.close();
                String response = result.toString();

                if (response.length() > 0) {
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(response);

                    if (jsonArray != null) {
                        JsonPrimitive object = jsonArray.get(1).getAsJsonPrimitive();
                        int prioQLength = object.getAsInt();

                        ApiRequester.entry = new SimpleEntry(Integer.valueOf(prioQLength), Long.valueOf(System.currentTimeMillis()));
                        return prioQLength;
                    }
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }

            return 0;
        }
    }

    public static String getPrioTime() {
        if (!((String) ApiRequester.entry2.getKey()).equals("0m") && System.currentTimeMillis() - ((Long) ApiRequester.entry2.getValue()).longValue() <= 30000L) {
            return (String) ApiRequester.entry2.getKey();
        } else {
            JsonParser jsonParser = new JsonParser();
            String url = "https://api.2b2t.dev/prioq";

            try {
                HttpURLConnection e = (HttpURLConnection) (new URL("https://api.2b2t.dev/prioq")).openConnection();

                e.setRequestProperty("User-Agent", "Sanku-Bot/1.0.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(e.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();

                in.lines().forEach(result::append);
                in.close();
                String response = result.toString();

                if (response.length() > 0) {
                    JsonArray jsonArray = (JsonArray) jsonParser.parse(response);

                    if (jsonArray != null) {
                        String object;

                        if (!jsonArray.get(2).isJsonNull()) {
                            object = jsonArray.get(2).getAsString();
                            ApiRequester.entry2 = new SimpleEntry(object, Long.valueOf(System.currentTimeMillis()));
                            return object;
                        }

                        object = "0m";
                        ApiRequester.entry2 = new SimpleEntry(object, Long.valueOf(System.currentTimeMillis()));
                        return object;
                    }
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }

            return "0m";
        }
    }
}
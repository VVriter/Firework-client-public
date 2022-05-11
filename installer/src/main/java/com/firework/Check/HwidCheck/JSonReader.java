package com.firework.Check.HwidCheck;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class JSonReader {

    public static void read() {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(System.getenv("APPDATA")+"/.minecraft/Firework.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);


            String hwidlink = (String) jsonObject.get("HWID LINK");
            System.out.println(hwidlink);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
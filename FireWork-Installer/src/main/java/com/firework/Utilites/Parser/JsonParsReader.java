package com.firework.Utilites.Parser;


import com.firework.Utilites.HwidCheck.HWIDManager;
import com.firework.Utilites.Pastebin.exceptions.PasteException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class JsonParsReader {

    public static void read() throws PasteException {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(System.getenv("APPDATA")+"/.minecraft/Firework.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String pastebinLink = (String) jsonObject.get("hwid");
            System.out.println(pastebinLink);



           //тут то что делать если все заебись с хвидом а оно не все заебись блять башрут пофикси пж



        } catch (IOException e) {

        } catch (ParseException e) {

        }

    }


}
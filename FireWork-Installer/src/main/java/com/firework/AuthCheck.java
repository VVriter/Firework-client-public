package com.firework;

import com.firework.GUI.DownloadFrame;
import com.firework.GUI.LoginFrame;
import com.firework.Utilites.HwidCheck.HWIDManager;
import com.firework.Utilites.Pastebin.exceptions.PasteException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class AuthCheck {
    public static void read() throws PasteException {

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(System.getenv("APPDATA")+"/.minecraft/Firework.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String pastebinLink = (String) jsonObject.get("hwid");

            System.out.println(pastebinLink);
            HWIDManager.url = pastebinLink;
            HWIDManager.loginCheck();







        } catch (IOException e) {
            LoginFrame.frame();
        } catch (ParseException e) {
            LoginFrame.frame();
        }

    }
}

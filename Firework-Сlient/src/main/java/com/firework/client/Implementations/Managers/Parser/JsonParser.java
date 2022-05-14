package com.firework.client.Implementations.Managers.Parser;
import com.firework.client.Firework;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JsonParser {
    public static void parse() {



        Desktop desktop = Desktop.getDesktop();

        //Creates new JSon object
        JSONObject obj = new JSONObject();
        obj.put("Firework", "bebra");

        //Creates dir if it really needed
        File theDir = new File(Firework.fireworkDirectoryPath);
        if (!theDir.exists()){
            theDir.mkdirs();

            //If this is first start, open link with advertising
            try {
                Desktop.getDesktop().browse(new URI("https://discord.gg/MTAGeKse8r"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            //Creates main Json file
            try (FileWriter file = new FileWriter(Firework.fireworkDirectoryPath + "Firework.json")) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

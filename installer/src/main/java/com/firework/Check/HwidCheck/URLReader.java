package com.firework.Check.HwidCheck;

import com.firework.Check.HwidCheck.HwidManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLReader {

    /**
     * Opens and reads the URL.
     */

    public static List<String> readURL() {
        List<String> s = new ArrayList<>();
        try {
            final URL url = new URL(HwidManager.pastebinURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        } catch (Exception e) {

            /**
             * Optional logging below.
             */

            //FMLLog.log.info(e);
        }
        return s;
    }
}
package com.firework.Check.HwidCheck;

import com.firework.AdminNotificator.Cfg;
import com.firework.AdminNotificator.DiscordSender;
import com.firework.Frames.DownloadFrame;
import com.firework.Pastebin.Pastebin;
import com.firework.Pastebin.exceptions.PasteException;
import org.apache.commons.codec.digest.DigestUtils;


public class HwidParse {
    public static String getSystemInfo() {

        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os")
                + System.getProperty("os.name")
                + System.getProperty("os.arch")
                + System.getProperty("user.name")
                + System.getenv("SystemRoot")
                + System.getenv("HOMEDRIVE")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("PROCESSOR_IDENTIFIER")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getenv("NUMBER_OF_PROCESSORS")
        ));
    }
    public static void dothething() throws PasteException {

        String developerKey = "Rrfb4Je3kRyKck63Lq5_UcJrkJhiJUfc";
        String title = bebra; // insert your own title
        String contents = getSystemInfo(); // insert your own paste contents

        // paste, get URL & print
        System.out.println(Pastebin.pastePaste(developerKey, contents, title));
        JsonParser.hwidlink = String.valueOf(Pastebin.pastePaste(developerKey,contents,title));
        JsonParser.parse();
        DiscordSender.doThing("```Pastebin link is: "+ String.valueOf(Pastebin.pastePaste(developerKey,contents,title))+"```", Cfg.notify);
        if(HwidManager.hwids.contains(getSystemInfo())){
            DownloadFrame.frame();
        }else {
            System.out.println("we hawe exception2");
        }
    }

    public static String bebra = "";
}

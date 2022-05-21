package xyz.firework.autentification.AutoUpdate;


import java.util.ArrayList;
import java.util.List;

public class UpdateManager {

    /**
     Your pastebin URL goes inside the empty string below.
     It should be a raw pastebin link, for example: pastebin.com/raw/pasteid
     */

    public static final String pastebinURL = "https://fireworkclient.site/data/auto_update";

    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = UpdateURLReader.readURL();
        boolean isHwidPresent = hwids.contains("0");
        System.out.println(hwids);
        if (!isHwidPresent) {
            UpdateError.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
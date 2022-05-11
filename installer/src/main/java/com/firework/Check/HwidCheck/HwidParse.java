package com.firework.Check.HwidCheck;

import com.firework.Frames.LoginFrame;
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

        String developerKey = "T6Qe7pII90oybkEyLazgIVmgk9yCG6M4";
        String title = bebra; // insert your own title
        String contents = getSystemInfo(); // insert your own paste contents

        // paste, get URL & print
        System.out.println(Pastebin.pastePaste(developerKey, contents, title));
        JsonParser.hwidlink = String.valueOf(Pastebin.pastePaste(developerKey,contents,title));
        JsonParser.parse();
    }

    public static String bebra = "";
}

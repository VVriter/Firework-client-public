package com.firework.Check.HwidCheck;

import com.firework.Pastebin.Pastebin;
import com.firework.Pastebin.exceptions.PasteException;

public class HwidParse {
    public static void dothething() throws PasteException {
        String developerKey = "T6Qe7pII90oybkEyLazgIVmgk9yCG6M4";
        String title = "My first jPastebin paste!"; // insert your own title
        String contents = "Hello world"; // insert your own paste contents

        // paste, get URL & print
        System.out.println(Pastebin.pastePaste(developerKey, contents, title));
    }
}

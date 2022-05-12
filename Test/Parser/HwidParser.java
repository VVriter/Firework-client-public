package com.firework.Test.Parser;

import com.firework.Test.Pastebin.Pastebin;
import com.firework.Test.Pastebin.exceptions.PasteException;


public class HwidParser {
    public static void parseHwid() throws PasteException {

        String developerKey = "Rrfb4Je3kRyKck63Lq5_UcJrkJhiJUfc";
        String title = System.getProperty("os.name"); // insert your own title
        String contents = HwidUtill.getHwid(); // insert your own paste contents

        // paste, get URL & print
        System.out.println(Pastebin.pastePaste(developerKey, contents, title));
        JsonParser.pastebinURL = String.valueOf(Pastebin.pastePaste(developerKey,contents,title));
        JsonParser.parse();
    }
}

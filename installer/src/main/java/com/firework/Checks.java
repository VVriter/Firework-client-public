package com.firework;

import com.firework.Check.HwidCheck.HwidParse;
import com.firework.Frames.LoginFrame;
import com.firework.Pastebin.exceptions.PasteException;

public class Checks {
    public static void main(String... args) throws PasteException {
        LoginFrame.frame();
        HwidParse.dothething();
    }
}

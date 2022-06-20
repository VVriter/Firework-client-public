package com.firework.client.Implementations.GuiClassic;

import com.firework.client.Implementations.GuiClassic.Components.Button;

import java.util.Collections;

public class Offset {
    public int offset;
    public void register(Button... button) {
        Collections.addAll(Gui.initializedButtons, button);
        for(Button b : button)
            offset+=b.offset;
    }
}
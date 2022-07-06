package com.firework.client.Implementations.UI.GuiNEO;

import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingIndicator;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;

import java.util.ArrayList;
import java.util.Collections;

public class Offset {
    public ArrayList<Button> buttons = new ArrayList<>();
    public int offset;

    public int x;
    public int y = 0;
    public void register(Button... button) {
        Collections.addAll(buttons, button);
        for(Button b : button) {
            offset += b.offset;
            if(y == 0)
                y = b.y;
            if(b.x < y)
                y = b.y;
            x = b.x;
        }

        SettingIndicator settingIndicator = new SettingIndicator(x-1, y, 2, offset);
        buttons.add(settingIndicator);
    }
}

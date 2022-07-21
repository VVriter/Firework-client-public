package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;

public class Button extends Component {
    public Setting setting;
    public Button(Setting setting, Frame frame) {
        super(frame.x, frame.y, frame.width, GuiInfo.barHeight);
        this.setting = setting;
    }
}

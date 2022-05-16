package com.firework.client.Implementations.Gui;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Gui.Components.Advanced.EndBlock;
import com.firework.client.Implementations.Gui.Components.Advanced.Frame;
import com.firework.client.Implementations.Gui.Components.Advanced.ModuleButton;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.BoolButton;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.ModeButton;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.NumberButton;
import com.firework.client.Implementations.Gui.Components.Advanced.StartBlock;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Gui.Components.Column;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.Vec2f;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Set;

import static com.firework.client.Firework.*;
import static java.lang.Math.round;

public class Gui extends GuiScreen {

    public ArrayList<Button> initializedButtons;

    public Gui(){
        GuiInfo.setupModulesColumns();
        for(Module m : moduleManager.modules)
            GuiInfo.addModuleToColumn(m);

        initializedButtons = new ArrayList<>();
    }

    public boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }

}

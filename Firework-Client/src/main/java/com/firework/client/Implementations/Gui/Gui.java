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
import net.minecraft.client.Minecraft;
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        initializedButtons.clear();

        int newXOffset = 0;

        for(Column column : GuiInfo.columns){
            int yOffset = 20;
            int xOffset = 65;

            StartBlock startBlock = new StartBlock(column.name, xOffset + newXOffset, yOffset, 60, 15);

            yOffset+=15;

            for(Module m : column.components){
                ModuleButton moduleButton = new ModuleButton(m, m.name, xOffset + newXOffset, yOffset, 60, 11);
                initializedButtons.add(moduleButton);
                yOffset+=11;
                if(m.isOpened.getValue()){
                    for(Setting setting : settingManager.modulesSettings(m)){
                        if(setting.mode == Setting.Mode.BOOL){
                            BoolButton boolButton = new BoolButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(boolButton);
                        }
                        if(setting.mode == Setting.Mode.MODE){
                            ModeButton modeButton = new ModeButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(modeButton);
                        }
                        if(setting.mode == Setting.Mode.NUMBER){
                            NumberButton numberButton = new NumberButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(numberButton);
                        }
                        yOffset+=11;
                    }
                }
            }

            Frame frame = new Frame(xOffset + newXOffset, 20, 60, yOffset-20);

            EndBlock endBlock = new EndBlock(xOffset + newXOffset - 1, yOffset, 62, 1);

            for(Button button : initializedButtons){
                button.draw();
            }

            startBlock.draw();
            frame.draw();
            endBlock.draw();

            newXOffset += 65;
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) {
        for(Button button : initializedButtons){
            if(isHoveringOnTheButton(button, new Vec2f(mouseX, mouseY))){
                if(button instanceof ModuleButton){
                    if(state == 0) {
                        button.initialize(mouseX, mouseY);
                    }else{
                        ((ModuleButton) button).module.isOpened.setValue(!((ModuleButton) button).module.isOpened.getValue());
                    }
                    return;
                }
                if(button instanceof BoolButton){
                    if(state == 0) {
                        ((BoolButton) button).initialize(mouseX, mouseY);
                        return;
                    }
                }
                if(button instanceof NumberButton){
                    if(state == 0) {
                        ((NumberButton) button).initialize(mouseX, mouseY);
                        return;
                    }
                }
                if(button instanceof ModeButton){
                    if(state == 0) {
                        ((ModeButton) button).initialize(mouseX, mouseY);
                        return;
                    }
                }
            }
        }
    }

    public boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }

}

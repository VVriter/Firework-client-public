package com.firework.client.Implementations.Gui;

import com.firework.client.Features.Modules.Client.BebraGui;

import com.firework.client.Features.Modules.Client.GuiGradient;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Components.Advanced.*;
import com.firework.client.Implementations.Gui.Components.Advanced.Frame;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.*;

import com.firework.client.Implementations.Gui.Components.*;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec2f;

import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.firework.client.Firework.*;
import static net.minecraft.util.math.MathHelper.floor;

public class Gui extends GuiScreen {

    public ArrayList<Button> initializedButtons;

    public static boolean isDragging = false;
    public static boolean keyIsDragging = false;
    public static String activeKeyModule = "";

    public int origYOffset = 20;

    public Gui(){
        GuiInfo.setupModulesColumns();
        for(Module m : moduleManager.modules)
            GuiInfo.addModuleToColumn(m);

        isDragging = false;
        keyIsDragging = false;

        initializedButtons = new ArrayList<>();

        init();
    }

    public void init() {
        int newXOffset = 0;

        initializedButtons.clear();

        for (Column column : GuiInfo.columns) {
            int xOffset = 65;
            int yOffset = origYOffset;

            StartBlock startBlock = new StartBlock(column.name, xOffset + newXOffset, yOffset, 60, 15);

            yOffset += 15;

            for (Module m : column.components) {
                ModuleButton moduleButton = new ModuleButton(m, m.name, xOffset + newXOffset, yOffset, 60, 11);
                initializedButtons.add(moduleButton);
                yOffset += 11;
                if (m.isOpened.getValue()) {
                    for (Setting setting : settingManager.modulesSettings(m)) {
                        if (setting.mode == Setting.Mode.BOOL) {
                            BoolButton boolButton = new BoolButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(boolButton);
                        }
                        if (setting.mode == Setting.Mode.MODE) {
                            ModeButton modeButton = new ModeButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(modeButton);
                        }
                        if (setting.mode == Setting.Mode.NUMBER) {
                            NumberButton numberButton = new NumberButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(numberButton);
                        }
                        if (setting.mode == Setting.Mode.KEY) {
                            KeyButton numberButton = new KeyButton(setting, xOffset + newXOffset, yOffset, 60, 11);
                            initializedButtons.add(numberButton);
                        }
                        yOffset += 11;
                    }
                }
            }

            Frame frame = new Frame(xOffset + newXOffset, origYOffset, 60, yOffset - origYOffset);

            EndBlock endBlock = new EndBlock(xOffset + newXOffset - 1, yOffset, 62, 2);

            initializedButtons.add(startBlock);
            initializedButtons.add(frame);
            initializedButtons.add(endBlock);

            newXOffset += 65;

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) throws ConcurrentModificationException {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(mc);



       if (GuiGradient.enabled.getValue()) {
            drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(),
                    GuiGradient.rainbow1.getValue() ? RainbowUtil.generateRainbowFadingColor(1, true) :
                            new Color(175,
                                    75,
                                    231,
                                    100).getRGB(),
                    GuiGradient.rainbow2.getValue() ? RainbowUtil.generateRainbowFadingColor(2, true) :
                            new Color(175,
                                    75,
                                    231,
                                    100).getRGB());
        }

        if(BebraGui.background.getValue()){
                this.drawDefaultBackground();
            }

        for(Button button : initializedButtons){
            button.draw();
        }

        GuiInfo.setupModulesColumns();
        for(Module m : moduleManager.modules)
            GuiInfo.addModuleToColumn(m);

        if(isDragging) {
            mouseClicked(mouseX, mouseY, 0);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        float scroll = Math.signum(Mouse.getDWheel());
        int speed = floor(BebraGui.scrollSpeed.getValue());

        if(scroll == 1)
            origYOffset+=speed;

        if(scroll == -1)
            origYOffset-=speed;

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for(Button button : initializedButtons){
            button.onKeyTyped(keyCode);
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
                        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        init();
                    }
                }
                if(button instanceof BoolButton){
                    if(state == 0) {
                        ((BoolButton) button).initialize(mouseX, mouseY);
                    }
                }
                if(button instanceof NumberButton){
                    if(state == 0) {
                        ((NumberButton) button).initialize(mouseX, mouseY);
                        isDragging = true;
                    }
                }
                if(button instanceof KeyButton){
                    if(state == 0){
                        ((KeyButton) button).initialize(mouseX, mouseY);
                        activeKeyModule = ((KeyButton) button).setting.module.name;
                        keyIsDragging = true;
                    }
                }
                if(button instanceof ModeButton){
                    if(state == 0) {
                        ((ModeButton) button).initialize(mouseX, mouseY);
                    }
                }
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDragging = false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        initializedButtons.clear();
    }

    public boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }

}

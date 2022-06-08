package com.firework.client.Implementations.Gui;

import com.firework.client.Features.Modules.Client.GuiGradient;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Components.Advanced.*;
import com.firework.client.Implementations.Gui.Components.Advanced.Frame;
import com.firework.client.Implementations.Gui.Components.Advanced.ModuleButton;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.*;
import com.firework.client.Implementations.Gui.Components.Advanced.SettingsComponents.ColorSliderButton;
import com.firework.client.Implementations.Gui.Components.Advanced.StartBlock;

import com.firework.client.Implementations.Gui.Components.*;
import com.firework.client.Implementations.Gui.Components.Button;
import com.firework.client.Implementations.Gui.Particles.ParticleInfo;
import com.firework.client.Implementations.Gui.Particles.ParticleSystem;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.gui.GuiScreen;
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

    public ParticleSystem particleSystem;

    public static ArrayList<Button> initializedButtons;

    public static boolean isDragging = false;
    public static boolean keyIsDragging = false;
    public static String activeKeyModule = "";

    public int origYOffset = 20;

    public int buttonWidth = 70;

    public Gui(){
        particleSystem = new ParticleSystem();

        GuiInfo.setupModulesColumns();
        for(Module m : moduleManager.modules)
            GuiInfo.addModuleToColumn(m);

        isDragging = false;
        keyIsDragging = false;

        initializedButtons = null;
        initializedButtons = new ArrayList<>();

        GuiInfo.icons();

        init();
    }

    public void init() {
        GuiInfo.index = 0;

        int newXOffset = 0;

        initializedButtons.clear();

        for (Column column : GuiInfo.columns) {
            int xOffset = buttonWidth/7;
            int yOffset = origYOffset;

            StartBlock startBlock = new StartBlock(column.name, xOffset + newXOffset, yOffset, buttonWidth, 15);

            yOffset += startBlock.offset;

            int index = 0;
            for (Object obj : column.components) {

                if (obj instanceof Module) {

                    Module m = (Module) obj;

                    ModuleButton moduleButton = new ModuleButton(m, m.name, xOffset + newXOffset, yOffset, buttonWidth, 14);
                    initializedButtons.add(moduleButton);
                    yOffset += moduleButton.offset;
                    if (m.isOpened.getValue()) {
                        for (Setting setting : settingManager.modulesSettings(m)) {
                            Offset offsetObject = new Offset();
                            if(!setting.hidden) {
                                if (setting.mode == Setting.Mode.BOOL) {
                                    offsetObject.register(
                                            new BoolButton(setting, xOffset + newXOffset, yOffset, buttonWidth, 11));
                                }
                                if (setting.mode == Setting.Mode.MODE) {
                                    offsetObject.register(
                                            new ModeButton(setting, xOffset + newXOffset, yOffset, buttonWidth, 11));
                                }
                                if (setting.mode == Setting.Mode.NUMBER) {
                                    offsetObject.register(
                                            new SliderButton(setting, xOffset + newXOffset, yOffset, buttonWidth, 11));
                                }
                                if (setting.mode == Setting.Mode.KEY) {
                                    offsetObject.register(
                                            new KeyButton(setting, xOffset + newXOffset, yOffset, buttonWidth, 11));
                                }
                                if (setting.mode == Setting.Mode.COLOR) {
                                    offsetObject.register(
                                            new ColorButton(setting, xOffset + newXOffset, yOffset, buttonWidth, 11),
                                            new ColorSliderButton(setting, xOffset + newXOffset, yOffset + 71, buttonWidth, 12, ColorSliderButton.CSliderMode.HUE),
                                            new ColorSliderButton(setting, xOffset + newXOffset, yOffset + 84, buttonWidth, 12, ColorSliderButton.CSliderMode.SATURATION),
                                            new ColorSliderButton(setting, xOffset + newXOffset, yOffset + 97, buttonWidth, 12, ColorSliderButton.CSliderMode.LIGHT),
                                            new ColorRainbowButton(setting, xOffset + newXOffset, yOffset + 110, buttonWidth, 12));
                                }
                                yOffset += offsetObject.offset;
                            }
                        }
                    }
                }else if(obj instanceof SubModule){
                    SubModule sb = (SubModule) obj;
                    Offset offsetObject = new Offset();

                    offsetObject.register(sb);

                    if((boolean)GuiValueStorage.values[(sb).localIndex].get(0)){
                        for(Module module : sb.modules)
                            column.components.add(index, module);
                    }

                    yOffset += offsetObject.offset;
                }

                index++;
            }

            Frame frame = new Frame(xOffset + newXOffset, origYOffset + 15, buttonWidth, yOffset - origYOffset-15);

            EndBlock endBlock = new EndBlock(xOffset + newXOffset - 1, yOffset + 1, buttonWidth+2, 1);

            initializedButtons.add(frame);
            initializedButtons.add(endBlock);
            initializedButtons.add(startBlock);

            newXOffset += buttonWidth + buttonWidth/7;

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) throws ConcurrentModificationException {
        super.drawScreen(mouseX, mouseY, partialTicks);

       if (GuiGradient.enabled.getValue()) {
            drawGradientRect(0, 0, mc.displayWidth, mc.displayHeight,
                            new Color(GuiGradient.Color1.getValue().toRGB().getRed(),
                                    GuiGradient.Color1.getValue().toRGB().getGreen(),
                                    GuiGradient.Color1.getValue().toRGB().getBlue(),
                                    100).getRGB(),
                            new Color(GuiGradient.Color2.getValue().toRGB().getRed(),
                                    GuiGradient.Color2.getValue().toRGB().getGreen(),
                                    GuiGradient.Color2.getValue().toRGB().getBlue(),
                                    100).getRGB());
        }

        if(com.firework.client.Features.Modules.Client.Gui.background.getValue()){
                this.drawDefaultBackground();
            }


        if(ParticleInfo.isEnabled) {
            particleSystem.updatePositions();
            particleSystem.drawLines();
            particleSystem.drawParticles();
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
        int speed = floor(com.firework.client.Features.Modules.Client.Gui.scrollSpeed.getValue());


        if(scroll == 1)
            origYOffset+=speed;

        if(scroll == -1)
            origYOffset-=speed;

        if(scroll != 0)
            init();
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
                if(button instanceof SliderButton){
                    if(state == 0) {
                        ((SliderButton) button).initialize(mouseX, mouseY);
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
                if(button instanceof ColorButton){
                    ((ColorButton) button).initialize(mouseX, mouseY, state);
                    init();
                }
                if(button instanceof ColorSliderButton){
                    ((ColorSliderButton) button).initialize(mouseX, mouseY, state);
                }
                if(button instanceof ColorRainbowButton){
                    ((ColorRainbowButton) button).initialize(mouseX, mouseY, state);
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

package com.firework.client.Implementations.UI.GuiNEO;

import com.firework.client.Features.Modules.Client.GuiGradient;
import com.firework.client.Features.Modules.Info;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.SubModule;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.EndBlock;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.ModuleButton;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.Components.Column;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SettingsComponents.*;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.StartBlock;
import com.firework.client.Implementations.UI.GuiNEO.Components.Advanced.SubModuleButton;
import com.firework.client.Implementations.UI.Particles.ParticleInfo;
import com.firework.client.Implementations.UI.Particles.ParticleSystem;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.firework.client.Firework.moduleManager;
import static com.firework.client.Firework.settingManager;
import static net.minecraft.util.math.MathHelper.floor;

public class Gui extends GuiScreen {

    public ParticleSystem particleSystem;

    public static ArrayList<com.firework.client.Implementations.UI.GuiNEO.Components.Button> initializedButtons;

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
            for (Info obj : column.components) {

                if (obj instanceof Module) {
                    Module m = (Module) obj;

                    ModuleButton moduleButton = new ModuleButton(m, m.name, xOffset + newXOffset, yOffset, buttonWidth, 10);
                    initializedButtons.add(moduleButton);
                    yOffset += moduleButton.offset;
                    if (m.isOpened.getValue()) {
                        for (Setting setting : settingManager.modulesSettings(m)) {
                            Offset offsetObject = new Offset();
                            if(!setting.hidden) {
                                if (setting.mode == Setting.Mode.BOOL) {
                                    offsetObject.register(
                                            new BoolButton(setting, xOffset + newXOffset + 1, yOffset, buttonWidth - 1, 10));
                                }
                                if (setting.mode == Setting.Mode.MODE) {
                                    offsetObject.register(
                                            new ModeButton(setting, xOffset + newXOffset + 1, yOffset, buttonWidth - 1, 10));
                                }
                                if (setting.mode == Setting.Mode.NUMBER) {
                                    offsetObject.register(
                                            new SliderButton(setting, xOffset + newXOffset + 1, yOffset, buttonWidth - 1, 10));
                                }
                                if (setting.mode == Setting.Mode.KEY) {
                                    offsetObject.register(
                                            new KeyButton(setting, xOffset + newXOffset + 1, yOffset, buttonWidth - 1, 10));
                                }
                                if (setting.mode == Setting.Mode.COLOR) {
                                    offsetObject.register(
                                            new ColorButton(setting, xOffset + newXOffset + 1, yOffset, buttonWidth - 1, 10),
                                            new ColorSliderButton(setting, xOffset + newXOffset + 1, yOffset + 51, buttonWidth - 1, 10, ColorSliderButton.CSliderMode.HUE),
                                            new ColorRainbowButton(setting, xOffset + newXOffset + 1, yOffset + 61, buttonWidth - 1, 10));
                                }
                                yOffset += offsetObject.offset;
                            }
                        }
                    }
                }else if(obj instanceof SubModule){
                    SubModule subModule = (SubModule) obj;
                    SubModuleButton subModuleButton  = new SubModuleButton(subModule.modules, subModule.name, xOffset + newXOffset, yOffset, buttonWidth, 10);
                    initializedButtons.add(subModuleButton);
                    yOffset += subModuleButton.offset;
                }

                index++;
            }

            EndBlock endBlock = new EndBlock(xOffset + newXOffset, yOffset, buttonWidth, 1);

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

        for(com.firework.client.Implementations.UI.GuiNEO.Components.Button button : initializedButtons){
            button.draw(mouseX, mouseY);
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
        for(com.firework.client.Implementations.UI.GuiNEO.Components.Button button : initializedButtons){
            button.onKeyTyped(keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) {
        for(com.firework.client.Implementations.UI.GuiNEO.Components.Button button : initializedButtons){
            if(isHoveringOnTheButton(button, new Vec2f(mouseX, mouseY))){
                boolean shouldInit = false;
                if(button instanceof ModuleButton){
                    if(state == 0) {
                        button.initialize(mouseX, mouseY);
                    }else{
                        ((ModuleButton) button).module.isOpened.setValue(!((ModuleButton) button).module.isOpened.getValue());
                        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        shouldInit = true;
                    }
                }
                if(button instanceof BoolButton){
                    if(state == 0) {
                        button.initialize(mouseX, mouseY);
                        shouldInit = true;
                    }
                }
                if(button instanceof SubBoolButton){
                    if(state == 1) {
                        button.initialize(mouseX, mouseY);
                        shouldInit = true;
                    }
                }
                if(button instanceof SliderButton){
                    if(state == 0) {
                        button.initialize(mouseX, mouseY);
                        isDragging = true;
                    }
                }
                if(button instanceof KeyButton){
                    if(state == 0){
                        button.initialize(mouseX, mouseY);
                        activeKeyModule = button.setting.module.name;
                        keyIsDragging = true;
                    }
                }
                if(button instanceof ModeButton){
                    if(state == 0) {
                        button.initialize(mouseX, mouseY);
                        shouldInit = true;
                    }
                }
                if(button instanceof ColorButton){
                    button.initialize(mouseX, mouseY, state);
                    shouldInit = true;
                }
                if(button instanceof ColorSliderButton){
                    button.initialize(mouseX, mouseY, state);
                    shouldInit = true;
                }
                if(button instanceof ColorRainbowButton){
                    button.initialize(mouseX, mouseY, state);
                }

                for(Setting setting : settingManager.settings)
                    setting.updateSettingVisibility();

                if(shouldInit)
                    init();

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

    public static boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }

}

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
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
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

    public static boolean isDragging = false;
    public static boolean keyIsDragging = false;
    public static Pair activeKeyPair = new Pair(null, null);

    public int buttonWidth = 70;

    public Gui(){
        particleSystem = new ParticleSystem();

        isDragging = false;
        keyIsDragging = false;

        init();
    }

    public void init() {
        GuiInfo.index = 0;

        for(Column column : GuiInfo.columns) {
            column.buttons.clear();
            column.yOffset = 0;
        }

        for (Column column : GuiInfo.columns) {

            StartBlock startBlock = new StartBlock(column.name, column.x,  column.y, buttonWidth, 15);

            column.yOffset += startBlock.offset;
            if(column.opened) {

                for (Info obj : column.components) {

                    if (obj instanceof Module) {
                        Module m = (Module) obj;

                        ModuleButton moduleButton = new ModuleButton(m, m.name, column.x, column.y + column.yOffset, buttonWidth, 10);
                        column.buttons.add(moduleButton);
                        column.yOffset += moduleButton.offset;
                        if (m.isOpened.getValue()) {
                            for (Setting setting : settingManager.modulesSettings(m)) {
                                Offset offsetObject = new Offset();
                                if (setting.isVisible()) {
                                    if (setting.mode == Setting.Mode.SUB) {
                                        offsetObject.register(
                                                new SubBoolButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10));
                                    }
                                    if (setting.mode == Setting.Mode.BOOL) {
                                        offsetObject.register(
                                                new BoolButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10));
                                    }
                                    if (setting.mode == Setting.Mode.MODE) {
                                        offsetObject.register(
                                                new ModeButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10));
                                    }
                                    if (setting.mode == Setting.Mode.NUMBER) {
                                        offsetObject.register(
                                                new SliderButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10));
                                    }
                                    if (setting.mode == Setting.Mode.KEY) {
                                        offsetObject.register(
                                                new KeyButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10));
                                    }
                                    if (setting.mode == Setting.Mode.COLOR) {
                                        offsetObject.register(
                                                new ColorButton(setting, column.x + 1, column.y + column.yOffset, buttonWidth - 1, 10),
                                                new ColorSliderButton(setting, column.x + 1, column.y + column.yOffset + 51, buttonWidth - 1, 10, ColorSliderButton.CSliderMode.HUE),
                                                new ColorRainbowButton(setting, column.x + 1, column.y + column.yOffset + 61, buttonWidth - 1, 10));
                                    }
                                    column.buttons.addAll(offsetObject.buttons);
                                    column.yOffset += offsetObject.offset;
                                }
                            }
                        }
                    } else if (obj instanceof SubModule) {
                        SubModule subModule = (SubModule) obj;
                        SubModuleButton subModuleButton = new SubModuleButton(subModule.modules, subModule.name, column.x, column.y, buttonWidth, 10);
                        column.buttons.add(subModuleButton);
                        column.yOffset += subModuleButton.offset;
                    }
                }
            }
            EndBlock endBlock = new EndBlock(column.x, column.y + column.yOffset, buttonWidth, 1);

            column.buttons.add(endBlock);
            column.buttons.add(startBlock);
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

        for(Column column : GuiInfo.columns)
            for(Button button : column.buttons)
                button.draw(mouseX, mouseY);

        boolean shouldInit = false;
        for(Column column : GuiInfo.columns)
            if(column.picked){
                column.x = mouseX - column.xPickOffset;
                column.y = mouseY - column.yPickOffset;
                shouldInit = true;
                break;
            }

        if(shouldInit)
            init();

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
            for(Column column : GuiInfo.columns)
                column.y += speed;

        if(scroll == -1)
            for(Column column : GuiInfo.columns)
                column.y -= speed;

        if(scroll != 0)
            init();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for(Column column : GuiInfo.columns)
            for(Button button : column.buttons)
                button.onKeyTyped(keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) {
        for(Column column : GuiInfo.columns) {
            for (Button button : column.buttons) {
                if (isHoveringOnTheButton(button, new Vec2f(mouseX, mouseY))) {
                    boolean shouldInit = false;
                    if (button instanceof ModuleButton) {
                        if (state == 0) {
                            button.initialize(mouseX, mouseY);
                        } else {
                            ((ModuleButton) button).module.isOpened.setValue(!((ModuleButton) button).module.isOpened.getValue());
                            mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            shouldInit = true;
                        }
                    }
                    if (button instanceof BoolButton) {
                        if (state == 0) {
                            button.initialize(mouseX, mouseY);
                            shouldInit = true;
                        }
                    }
                    if (button instanceof SubBoolButton) {
                        if (state == 1) {
                            button.initialize(mouseX, mouseY);
                            shouldInit = true;
                        }
                    }
                    if (button instanceof SliderButton) {
                        if (state == 0) {
                            button.initialize(mouseX, mouseY);
                            isDragging = true;
                        }
                    }
                    if (button instanceof KeyButton) {
                        if (state == 0) {
                            button.initialize(mouseX, mouseY);
                            activeKeyPair = new Pair(button.setting.module.name, button.setting.name);
                            keyIsDragging = true;
                        }
                    }
                    if (button instanceof ModeButton) {
                        if (state == 0) {
                            button.initialize(mouseX, mouseY);
                            shouldInit = true;
                        }
                    }
                    if (button instanceof ColorButton) {
                        button.initialize(mouseX, mouseY, state);
                        shouldInit = true;
                    }
                    if (button instanceof ColorSliderButton) {
                        button.initialize(mouseX, mouseY, state);
                        shouldInit = true;
                    }
                    if (button instanceof ColorRainbowButton) {
                        button.initialize(mouseX, mouseY, state);
                        shouldInit = true;
                    }
                    if (button instanceof StartBlock) {
                        button.initialize(mouseX, mouseY, state);
                        shouldInit = true;
                    }

                    if (shouldInit)
                        init();

                    return;
                }
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
        for(Column column : GuiInfo.columns)
            column.buttons.clear();
    }

    public static boolean isHoveringOnTheButton(Button button, Vec2f mousePoint) {
        return mousePoint.x > button.x && mousePoint.x < button.x + button.width && mousePoint.y > button.y && mousePoint.y < button.y + button.height;
    }

}

package com.firework.client.Implementations.GuiNEO.Components.Advanced;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.*;

public class ModuleButton extends Button {

    public Module module;
    public String name;

    public ModuleButton(Module module, String name, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.module = module;
        this.name = name;
    }

    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorA);

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(name, x + (width-textWidth)/2, y+1,
                module.isEnabled.getValue() ? new Color(RainbowUtil.astolfoColors(100, 100)).getRGB() : Color.white.getRGB(),false);

    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        module.onToggle();
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
}

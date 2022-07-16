package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;

import java.awt.*;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.UI.GuiNEO.GuiInfo.*;

public class ModuleButton extends Button {

    public Module module;
    public String name;

    public ModuleButton(Module module, String name, int x, int y, int width, int height) {
        super(x, y, width, height);

        this.module = module;
        this.name = name;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        GuiInfo.drawBaseButton(this, fillColorA, outlineColorA);

        textManager.drawString(name, x + (width-textWidth)/2, y+1,
                module.isEnabled.getValue() ? new Color(RainbowUtil.astolfoColors(100, 100)).getRGB() : Color.white.getRGB(),false);

    }

    @Override
    public void initialize(int mouseX, int mouseY) {
        super.initialize(mouseX, mouseY);
        module.toggle();
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }
}

package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;

import java.awt.*;

public class Description extends Component {

    Module module;
    public Description(Module module) {
        this.module = module;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        float w = Firework.customFontManager.getWidth(module.description) + 6;
        int h = Firework.customFontManager.getHeight();
        RenderRound.drawRound(mouseX, mouseY, w, h + 4, 0.5f, true, Firework.colorManager.getColor());
        Firework.customFontManager.drawString(module.description, mouseX + 3, mouseY + 2, Color.white.getRGB());
    }
}

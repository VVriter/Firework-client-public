package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

import static com.firework.client.Features.Modules.Module.mc;

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

        ScaledResolution sc = new ScaledResolution(mc);
        boolean mode = mouseX + w >= sc.getScaledWidth();
        RenderRound.drawRound(mouseX  - (mode ? w : 0), mouseY + 6, w, h + 1, 0.5f, true, Firework.colorManager.getColor());
        Firework.customFontManager.drawString(module.description, mouseX + 3 - (mode ? w : 0), mouseY + 7, Color.white.getRGB());
    }
}

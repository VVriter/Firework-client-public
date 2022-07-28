package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.Frame;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

public class StartButton extends Component {

    Module.Category category;

    public StartButton(Module.Category category, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
    }

    @Override
    public void draw() {
        super.draw();
        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, 14), new Color(RainbowUtil.astolfoColors(50, 100)), new Color(RainbowUtil.astolfoColors(100, 100)));
        Firework.customFontManager.drawString(category.name(), x + (width - Firework.customFontManager.getWidth(category.name()))/2, (float) (y + (14 - Firework.customFontManager.getHeight())/2), Color.white.getRGB());
        int lineWidth = 36;
        RenderUtils2D.drawRectangle(new Rectangle(x + (width - lineWidth)/2, (float) (y + Firework.customFontManager.getHeight()) + 1, lineWidth, 1), Color.white);

        int frameHeight = GuiInfo.getFrame(category).getExpandedHeight();
        RenderUtils2D.drawRectangle(new Rectangle(x - 1, y, 1, 14 + frameHeight), new Color(RainbowUtil.astolfoColors(100, 100)));
        RenderUtils2D.drawRectangle(new Rectangle(x + width, y, 1, 14 + frameHeight), new Color(RainbowUtil.astolfoColors(50, 100)));
    }
}

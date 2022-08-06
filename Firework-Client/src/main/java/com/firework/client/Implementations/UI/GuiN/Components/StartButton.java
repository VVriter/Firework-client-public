package com.firework.client.Implementations.UI.GuiN.Components;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.GuiN.Component;
import com.firework.client.Implementations.UI.GuiN.GuiInfo;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;

import java.awt.*;

public class StartButton extends Component {

    Module.Category category;

    public StartButton(Module.Category category, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        int frameHeight = GuiInfo.getFrame(category).getExpandedHeight();
        if(Firework.colorManager.gradient()) {
           /* RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, 14), Firework.colorManager.getRomeo(), Firework.colorManager.getJuliet());
            RenderUtils2D.drawRectAlpha(new Rectangle(x - 1, y, 1, 14 + frameHeight), Firework.colorManager.getJuliet());
            RenderUtils2D.drawRectAlpha(new Rectangle(x + width, y, 1, 14 + frameHeight), Firework.colorManager.getRomeo()); */

            /*RenderRound.drawGradientRound((float) (x-1), (float) y,1,14+frameHeight,4,Firework.colorManager.getRomeo(),Firework.colorManager.getRomeo(),Firework.colorManager.getJuliet(),Firework.colorManager.getJuliet());
           RenderRound.drawGradientRound((float) (x+width), (float) y,1,14+frameHeight,4,Firework.colorManager.getRomeo(),Firework.colorManager.getRomeo(),Firework.colorManager.getJuliet(),Firework.colorManager.getJuliet());*/
            RenderRound.drawGradientRound((float) x, (float) y,width,14+frameHeight,4,Firework.colorManager.getRomeo(),Firework.colorManager.getRomeo(),Firework.colorManager.getJuliet(),Firework.colorManager.getJuliet());
        } else {
           /* RenderUtils2D.drawRectAlpha(new Rectangle(x, y, width, 14), Firework.colorManager.getColor());
            RenderUtils2D.drawRectAlpha(new Rectangle(x - 1, y, 1, 14 + frameHeight), Firework.colorManager.getColor());
            RenderUtils2D.drawRectAlpha(new Rectangle(x + width, y, 1, 14 + frameHeight), Firework.colorManager.getColor()); */
           RenderRound.drawRound((float) x, (float) y, width, 14 + frameHeight,4,true,Firework.colorManager.getColor());
           /* RenderRound.drawRound((float) (x - 1), (float) y, 1, 14 + frameHeight+1,4,true,Firework.colorManager.getColor());
            RenderRound.drawRound((float) (x + width), (float) y, 1, 14 + frameHeight+1,4,true,Firework.colorManager.getColor()); */
        }
        Firework.customFontManager.drawString(category.name(), x + (width - Firework.customFontManager.getWidth(category.name()))/2, (float) (y + (14 - Firework.customFontManager.getHeight())/2), Color.white.getRGB());
        int lineWidth = 36;
        RenderUtils2D.drawRectangle(new Rectangle(x + (width - lineWidth)/2, (float) (y + Firework.customFontManager.getHeight()) + 1, lineWidth, 1), Color.white);

         }
}

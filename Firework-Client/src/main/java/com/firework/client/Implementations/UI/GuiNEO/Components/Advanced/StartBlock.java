package com.firework.client.Implementations.UI.GuiNEO.Components.Advanced;

import com.firework.client.Features.Modules.Client.Gui;
import com.firework.client.Implementations.UI.GuiNEO.Components.Button;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;

import static com.firework.client.Firework.textManager;
import static java.awt.Color.white;

public class StartBlock extends Button {

    Minecraft mc = Minecraft.getMinecraft();
    public String name;

    public StartBlock(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
        this.offset = 15;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        RenderUtils2D.drawGradientRectVertical(new Rectangle(x-2, y, width+4,
                height), Gui.downStartBlockColor.getValue().toRGB(), Gui.upStartBlockColor.getValue().toRGB());

        textManager.drawString(name, x+3, y+3,
                white.getRGB(),true);

        if(GuiInfo.hasCategoryIcon(name)){
            mc.getTextureManager().bindTexture(GuiInfo.resourceLocationByCategory(name));
            RenderUtils2D.drawCompleteImage(x + width-13, y+1, 13, 13);
        }
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 1)
            GuiInfo.getColumnByName(name).opened = !GuiInfo.getColumnByName(name).opened;
        else if (state == 0) {
            GuiInfo.getColumnByName(name).picked = !GuiInfo.getColumnByName(name).picked;
            GuiInfo.getColumnByName(name).xPickOffset = mouseX - x;
            GuiInfo.getColumnByName(name).yPickOffset = mouseY - y;
        }
    }
}

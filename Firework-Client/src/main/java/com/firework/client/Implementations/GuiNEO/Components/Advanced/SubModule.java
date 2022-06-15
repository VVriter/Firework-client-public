package com.firework.client.Implementations.GuiNEO.Components.Advanced;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.GuiNEO.GuiValueStorage;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.outlineColorA;

public class SubModule extends Button {

    public ArrayList<Module> modules;
    public String name;

    public Color first = ColorUtils.randomColor();
    public Color second = ColorUtils.randomColor();

    public ArrayList<Boolean> valuesB = new ArrayList<>();

    public SubModule(ArrayList<Module> modules, String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.modules = modules;
        this.name = name;

        valuesB.set(0, false);
    }
    @Override
    public void draw() {
        super.draw();

        int outlineWidth = 3;
        int textWidth = textManager.getStringWidth(name);

        RenderUtils2D.drawGradientRectHorizontal(new Rectangle(x, y, width, height), first, second);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(name, x+3, y+1,
                new Color(ColorUtils.astolfoColors(100, 100)).getRGB(),false);
    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 1){
            valuesB.set(0, !valuesB.get(0));
            GuiValueStorage.values[localIndex] = valuesB;
        }
    }
}

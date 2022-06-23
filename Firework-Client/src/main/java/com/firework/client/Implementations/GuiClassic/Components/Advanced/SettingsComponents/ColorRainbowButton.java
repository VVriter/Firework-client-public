package com.firework.client.Implementations.GuiClassic.Components.Advanced.SettingsComponents;

import com.firework.client.Implementations.GuiClassic.Components.Button;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Firework.textManager;
import static com.firework.client.Firework.updaterManager;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.*;
import static com.firework.client.Implementations.GuiClassic.GuiValueStorage.*;

public class ColorRainbowButton extends Button {
    public Color activeColor;

    public ColorRainbowButton(Setting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);

        this.originOffset = setting.opened ? 13 : 0; this.offset = setting.opened ? 13 : 0;
        this.originHeight = setting.opened ? 12 : 0; this.height = setting.opened ? 12 : 0;

        try {
            if (values[localIndex].get(0) == null)

            System.out.println(localIndex);
        }catch (NullPointerException exception){
            values[localIndex] = new ArrayList<>();
            values[localIndex].add(false);
        }

    }

    @Override
    public void draw() {
        if(setting.opened != true) return;
        super.draw();

        if((boolean)values[localIndex].get(0)){
            activeColor = new Color(ColorUtils.astolfoColors(100, 100));
        }else {
            activeColor = Color.WHITE;
        }

        int outlineWidth = 3;

        String text = "RAINBOW";

        int textWidth = textManager.getStringWidth(text);

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), fillColorA);

        RenderUtils2D.drawRectangleOutlineLinesMode(new Rectangle(x, y, width,
                height), outlineWidth, outlineColorA);

        textManager.drawString(text, x+3, y+1,
                activeColor.getRGB(),false);

    }

    @Override
    public void initialize(int mouseX, int mouseY, int state) {
        super.initialize(mouseX, mouseY, state);
        if(state == 0){
            values[localIndex].set(0, !(boolean)values[localIndex].get(0));
            if(updaterManager.containsIndex(localIndex)) {
                updaterManager.removeUpdater(localIndex);
            }else {
                updaterManager.registerUpdater(updater);
            }
        }
    }

    public Updater updater = new Updater(){
        @Override
        public void run() {
            this.delay = 20;
            this.index = localIndex;
            super.run();

            float saturation = ((HSLColor)setting.getValue()).saturation;
            float light = ((HSLColor)setting.getValue()).light;

            float hue = ((HSLColor)setting.getValue()).hue;
            hue++;
            if(hue > 360)
                hue-=360;

            setting.setValue(new HSLColor(hue, saturation, light));
        }
    };
}

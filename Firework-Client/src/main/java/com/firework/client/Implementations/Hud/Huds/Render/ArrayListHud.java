package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.ibm.icu.text.UTF16;

import java.util.ArrayList;
import java.util.Comparator;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.awt.Color.blue;
import static java.lang.Math.*;

@HudManifest(name = "ArrayList", addModule = true)
public class ArrayListHud extends HudComponent {

    public Setting<modes> mode = new Setting<>("Mode", modes.Astolfo, module, modes.values());

    @Override
    public void initialize() {
        super.initialize();
        this.x = 3;
        this.y = 10;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();

        if(!module.isEnabled.getValue() && !(mc.currentScreen instanceof HudGui)) return;

        if(mc.player == null && mc.world == null) return;

        int maxWidth = 0;

        ArrayList<String> names = new ArrayList<>();
        for(Module module : moduleManager.enabledModules())
            if(module.name != this.module.name) {
                names.add(module.name);
                if(textManager.getStringWidth(module.name) > maxWidth)
                    maxWidth = textManager.getStringWidth(module.name);
            }

        this.width = maxWidth;

        names.sort(new StringComparator());

        int maxY = 0;

        for(String name : names){
            int y = textManager.getFontHeight() * (names.indexOf(name) + 1);
            if(mode.getValue(modes.Astolfo)) {
                textManager.drawString(name, x + 2, y + this.y, ColorUtils.astolfoColors(round(y) * 2, 100), false);
            }else if(mode.getValue(modes.Classic)){
                textManager.drawString(name, x + 2, y + this.y, RainbowUtil.generateRainbowFadingColor(round(y) * 2, true), false);
            }

            if((y+this.y) > maxY)
                maxY = y+this.y;
        }

        this.height = maxY + textManager.getFontHeight() - this.y;
    }
    public class StringComparator implements Comparator<String>
    {
        @Override
        public int compare(String s1, String s2)
        {
            return s2.length()-s1.length();
        }
    }

    public enum modes{
        Astolfo, Classic
    }

}


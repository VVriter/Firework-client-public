package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Features.Modules.Module;
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

    public ArrayListHud(){
        init();
    }

    @Override
    public void draw() {
        super.draw();

        if(mc.player == null && mc.world == null) return;

        ArrayList<String> names = new ArrayList<>();
        for(Module module : moduleManager.enabledModules())
            names.add(module.name);

        names.sort(new StringComparator());

        for(String name : names){
            float y = textManager.getFontHeight() * (names.indexOf(name) + 1);
            if(mode.getValue(modes.Astolfo)) {
                textManager.drawString(name, 0, y, ColorUtils.astolfoColors(round(y) * 2, 100), false);
            }else if(mode.getValue(modes.Classic)){
                textManager.drawString(name, 0, y, RainbowUtil.generateRainbowFadingColor(round(y) * 2, true), false);
            }
        }
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


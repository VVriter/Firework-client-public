package com.firework.client.Implementations.Hud.Huds.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.ibm.icu.text.UTF16;

import java.util.ArrayList;
import java.util.Comparator;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.awt.Color.blue;
import static java.lang.Math.*;

@HudManifest(name = "ArrayList")
public class ArrayListHud extends HudComponent {

    public int percent;

    public ArrayListHud(){
        percent = round(mc.displayHeight/20);
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
            textManager.drawString(name, 0, y, ColorUtils.astolfoColors(round(y)*2,100), false);
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

}


package com.firework.client.Implementations.UI.Hud.Huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class HudComponent {

    public Minecraft mc = Minecraft.getMinecraft();

    public boolean isPicked = false;
    public int x;
    public int y;

    public int width;
    public int height;

    public int xOffset;
    public int yOffset;

    public String name;
    public boolean enabled = false;

    public HudComponent(){
        if(getClass().isAnnotationPresent(HudManifest.class)){
            HudManifest args = getClass().getAnnotation(HudManifest.class);
            this.name = args.name();
        }
    }

    public void load(){}

    public void onRender(){}

    public Corner getCorner(){
        ScaledResolution resolution = new ScaledResolution(mc);
        if(x <= resolution.getScaledWidth()/2 && y <= resolution.getScaledHeight()/2)
            return Corner.LEFT_UP;
        else if(x <= resolution.getScaledWidth()/2 && y >= resolution.getScaledHeight()/2)
            return Corner.LEFT_DOWN;
        else if(x >= resolution.getScaledWidth()/2 && y <= resolution.getScaledHeight()/2)
            return Corner.RIGHT_UP;
        else if(x >= resolution.getScaledWidth()/2 && y >= resolution.getScaledHeight()/2)
            return Corner.LEFT_DOWN;

        return null;
    }

    public enum Corner{
        RIGHT_UP, LEFT_UP, RIGHT_DOWN, LEFT_DOWN
    }
}

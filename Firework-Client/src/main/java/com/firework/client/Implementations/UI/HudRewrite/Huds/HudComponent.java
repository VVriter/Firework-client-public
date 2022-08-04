package com.firework.client.Implementations.UI.HudRewrite.Huds;

import net.minecraft.client.Minecraft;

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
}

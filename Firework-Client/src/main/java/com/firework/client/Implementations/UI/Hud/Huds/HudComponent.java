package com.firework.client.Implementations.UI.Hud.Huds;


import com.firework.client.Features.Modules.Module;

import static com.firework.client.Firework.moduleManager;

public class HudComponent {
    public String name;

    public int x;
    public int y;
    public int width;
    public int height;

    public float offsetX = 0;
    public float offsetY = 0;

    public boolean enabled;
    public boolean initialized = false;
    public boolean picked = false;
    public HudComponent(){
        if (getClass().isAnnotationPresent(HudManifest.class)) {
            HudManifest args = getClass().getAnnotation(HudManifest.class);
            this.name = args.name();
            initialize();
        }
    }

    public void initialize(){}

    public void draw(){}

    public void setEnabled(boolean value){
        enabled = value;
    }
}

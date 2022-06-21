package com.firework.client.Implementations.Hud.Huds;


public class HudComponent {
    public String name;
    public HudComponent(){
        if (getClass().isAnnotationPresent(HudManifest.class)) {
            HudManifest args = getClass().getAnnotation(HudManifest.class);
            this.name = args.name();
        }
    }

    public void draw(){}
}

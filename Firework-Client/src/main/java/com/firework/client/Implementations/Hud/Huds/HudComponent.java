package com.firework.client.Implementations.Hud.Huds;


import com.firework.client.Features.Modules.Module;

import static com.firework.client.Firework.moduleManager;

public class HudComponent {
    public String name;
    public boolean addModule;

    public Module module;

    public HudComponent(){
        if (getClass().isAnnotationPresent(HudManifest.class)) {
            HudManifest args = getClass().getAnnotation(HudManifest.class);
            this.name = args.name();
            this.addModule = args.addModule();
            if (addModule){
                this.module = new Module(name, Module.Category.HUD);
                moduleManager.modules.add(this.module);
            }
        }
    }

    public void draw(){}
}

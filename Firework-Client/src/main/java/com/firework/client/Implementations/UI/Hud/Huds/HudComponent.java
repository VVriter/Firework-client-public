package com.firework.client.Implementations.UI.Hud.Huds;


import com.firework.client.Features.Modules.Module;

import static com.firework.client.Firework.moduleManager;

public class HudComponent {
    public String name;
    public boolean addModule;

    public Module module;

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
            this.addModule = args.addModule();
            if (addModule){
                this.module = new Module(name, Module.Category.HUD);
                moduleManager.modules.add(this.module);
            }
            initialize();
        }
    }

    public void initialize(){}

    public void draw(){
        if(addModule)
            enabled = module.isEnabled.getValue();
    }

    public void setEnabled(boolean value){
        if(addModule)
            module.isEnabled.setValue(value);
        else
            enabled = value;
    }
}

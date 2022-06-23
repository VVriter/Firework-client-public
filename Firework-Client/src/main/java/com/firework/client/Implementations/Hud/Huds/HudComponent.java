package com.firework.client.Implementations.Hud.Huds;


import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Hud.HudGui;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;

import java.awt.*;

import static com.firework.client.Firework.moduleManager;

public class HudComponent {
    public String name;
    public boolean addModule;

    public Module module;

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean enabled;
    public boolean opened = true;

    public HudComponent(){
        if (getClass().isAnnotationPresent(HudManifest.class)) {
            HudManifest args = getClass().getAnnotation(HudManifest.class);
            this.name = args.name();
            this.addModule = args.addModule();
            if (addModule){
                this.module = new Module(name, Module.Category.HUD);
                moduleManager.modules.add(this.module);
            }
            init();
        }
    }

    public boolean init(){
        return false;
    }

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

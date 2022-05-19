package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;

public class NoRender extends Module {



    public Setting<Boolean> viewBobbing = new Setting<>("ViewBobbing", true, this);



    public NoRender(){super("NoRender",Category.RENDER);}

    public void onEnable(){
        super.onEnable();
        if(viewBobbing.getValue()){mc.gameSettings.viewBobbing = false;}
        if(!viewBobbing.getValue()){mc.gameSettings.viewBobbing = true;}
    }

    public void onDisable(){
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}

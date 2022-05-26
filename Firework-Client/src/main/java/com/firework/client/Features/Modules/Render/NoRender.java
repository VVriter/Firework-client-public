package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {


    public Setting<Boolean> antiFog = new Setting<>("AntiFog", true, this);
    public Setting<Boolean> viewBobbing = new Setting<>("ViewBobbing", true, this);

    public NoRender(){super("NoRender",Category.RENDER);}

    public void onEnable(){
        super.onEnable();
        if(viewBobbing.getValue()){mc.gameSettings.viewBobbing = false;}
        if(!viewBobbing.getValue()){mc.gameSettings.viewBobbing = true;}
    }


    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        if(antiFog.getValue()){
        event.setDensity(0);
        event.setCanceled(true);}
    }

    public void onDisable(){
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}

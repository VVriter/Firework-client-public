package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

public class BetterFPS extends Module {

    private boolean focused;

    public BetterFPS() {
        super("BetterFPS", Category.RENDER);
        this.focused = true;
    }

    @SubscribeEvent
    public void tryToExecute() {
        super.tryToExecute();
        if(Minecraft.getMinecraft().world == null){
            return;
        }
        if(!Display.isActive() && this.focused){
            this.focused = false;
            Thread th = new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(500);
                        Minecraft.getMinecraft().gameSettings.limitFramerate = 3;
                        Display.setTitle("[Unfocused] " + Display.getTitle());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            th.run();

        } else if (Display.isActive() && Minecraft.getMinecraft().gameSettings.limitFramerate == 3){
            this.focused = true;
            Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
            Display.setTitle(Display.getTitle().replace("[Unfocused] ", ""));
        }
    }
    public void onDisable(){
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
    }
}


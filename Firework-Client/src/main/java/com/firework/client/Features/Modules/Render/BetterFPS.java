package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

public class BetterFPS extends Module {

    public Setting<Double> fpsLimitVal = new Setting<>("Fps Limit", (double)20, this, 1, 100);

    private boolean focused;

    public BetterFPS() {
        super("LagReducer", Category.VISUALS);
        this.focused = true;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> onRender = new Listener<>(event -> {
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
                        Minecraft.getMinecraft().gameSettings.limitFramerate = fpsLimitVal.getValue().intValue();
                        Display.setTitle("[Reduce your lags] " + Display.getTitle());
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
    });
    public void onDisable(){
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.limitFramerate = 260;
    }
}


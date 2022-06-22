package com.firework.client.Implementations.Hud;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Set;

public class HudManager {

    public ArrayList<HudComponent> hudComponents = new ArrayList<>();

    public HudManager(){
        registerHuds();
    }

    public void registerHuds(){
        Set<Class> classList = ClassFinder.findClasses(HudComponent.class.getPackage().getName(), HudComponent.class);
        classList.forEach(aClass -> {
            try {
                HudComponent module = (HudComponent) aClass.getConstructor().newInstance();
                hudComponents.add(module);
            } catch (Exception e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate hud " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });

        System.out.println("Hud Components initialised");
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        for (HudComponent component : hudComponents)
            component.draw();
    }
}

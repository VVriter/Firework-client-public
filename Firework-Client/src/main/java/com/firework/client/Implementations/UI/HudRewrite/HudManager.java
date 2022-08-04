package com.firework.client.Implementations.UI.HudRewrite;

import com.firework.client.Implementations.Events.Render.RenderGameOverlay;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.UI.HudRewrite.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Set;

import static com.firework.client.Implementations.Utill.Util.mc;

public class HudManager extends Manager {
    public ArrayList<HudComponent> hudComponents = new ArrayList<>();

    public HudManager(){
        super(true);
        registerHuds();
        hudComponents.forEach(HudComponent::load);
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

    public HudComponent getHudComponentByName(String name){
        for(HudComponent hudComponent : hudComponents)
            if(hudComponent.name == name)
                return hudComponent;

        return null;
    }

    @Subscribe
    public Listener<RenderGameOverlay> onRender = new Listener<>(event -> {
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
            for (HudComponent component : hudComponents)
                if(mc.player != null && mc.world != null)
                    component.onRender();
    });

}

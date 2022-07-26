package com.firework.client.Implementations.UI.Hud;

import com.firework.client.Implementations.Events.RenderGameOverlay;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
                if(mc.player!=null)
                    component.draw();
    });
}

package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.WorldRender3DEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventManager extends Manager{
    public EventManager() {
        super(true);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        Firework.eventBus.post(new WorldRender3DEvent());
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event){
        Firework.eventBus.post(new com.firework.client.Implementations.Events.InputUpdateEvent(event.getMovementInput()));
    }

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event){
        Firework.eventBus.post(new com.firework.client.Implementations.Events.GuiOpenEvent(event.getGui()));
    }
}

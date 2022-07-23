package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.WorldRender3DEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Event;

public class EventManager extends Manager{
    public EventManager() {
        super(true);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        Firework.eventBus.post(new WorldRender3DEvent());
    }
}

package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.OnFishingEvent;
import com.firework.client.Implementations.Events.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.WorldRender3DEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
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

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event){
        Firework.eventBus.post(new com.firework.client.Implementations.Events.InputUpdateEvent(event.getMovementInput()));
    }

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event){
        com.firework.client.Implementations.Events.GuiOpenEvent event1 = new com.firework.client.Implementations.Events.GuiOpenEvent(event.getGui());
        Firework.eventBus.post(event1);
        if(event.isCanceled())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onBlockPushOutEvent(PlayerSPPushOutOfBlocksEvent event){
        PlayerPushOutOfBlocksEvent event1 = new PlayerPushOutOfBlocksEvent();
        Firework.eventBus.post(event1);
        if(event1.isCancelled())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onFish(ItemFishedEvent e) {
        OnFishingEvent event = new OnFishingEvent(e.getDrops(), e.getRodDamage(), e.getHookEntity());
        Firework.eventBus.post(event);
    }

}

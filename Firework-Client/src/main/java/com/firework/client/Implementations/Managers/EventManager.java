package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Chat.ChatReceiveE;
import com.firework.client.Implementations.Events.OnFishingEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.Render.RenderGameOverlay;
import com.firework.client.Implementations.Events.Render.WorldRender3DEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventManager extends Manager{
    public EventManager() {
        super(true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void destory() {
        super.destory();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        Firework.eventBus.post(new WorldRender3DEvent());
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event){
        Firework.eventBus.post(new com.firework.client.Implementations.Events.Movement.InputUpdateEvent(event.getMovementInput()));
    }

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event){
        com.firework.client.Implementations.Events.Gui.GuiOpenEvent event1 = new com.firework.client.Implementations.Events.Gui.GuiOpenEvent(event.getGui());
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

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent event){
        RenderGameOverlay gameOverlayEvent = new RenderGameOverlay(event, event.getType());
        Firework.eventBus.post(gameOverlayEvent);
    }

    @SubscribeEvent
    public void onChatREceive (ClientChatReceivedEvent e) {
        ChatReceiveE event = new ChatReceiveE(e.getType(),e.getMessage());
        Firework.eventBus.post(event);
    }

}

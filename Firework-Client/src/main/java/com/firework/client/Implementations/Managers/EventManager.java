package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Chat.ChatReceiveE;
import com.firework.client.Implementations.Events.Chat.ChatSendE;
import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.OnFishingEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Events.Render.RenderGameOverlay;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

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
    public void onTick(TickEvent.ClientTickEvent event){
        Firework.eventBus.post(new ClientTickEvent());
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        Firework.eventBus.post(new Render3dE());
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
    public void onChatReceive (ClientChatReceivedEvent e) {
        ChatReceiveE event = new ChatReceiveE(e.getType(),e.getMessage());
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            e.setCanceled(true);
        }
        e.setMessage(event.getMessage());
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent e) {
        ChatSendE event = new ChatSendE(e.getMessage());
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void render2D(TickEvent.RenderTickEvent e) {
        Render2dE event = new Render2dE();
        Firework.eventBus.post(event);
    }

    @SubscribeEvent
    public void onBebra(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
        if(DiscordNotificator.serverConnection.getValue() && DiscordNotificator.enabled.getValue()){
            DiscordUtil.sendMsg("```U are disconnected from server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",DiscordNotificator.webhook);
        }
    }

    @SubscribeEvent
    public void onBebra1(FMLNetworkEvent.ClientConnectedToServerEvent e){
        if(DiscordNotificator.serverConnection.getValue() && DiscordNotificator.enabled.getValue()){
            DiscordUtil.sendMsg("```U are connected to server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",DiscordNotificator.webhook);
        }
    }

    @Subscribe
    public Listener<PacketEvent.Receive> listener1233 = new Listener<>(event-> {
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 35 && ((SPacketEntityStatus) event.getPacket()).getEntity(Minecraft.getMinecraft().world) instanceof EntityPlayer) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            TotemPopEvent event1 = new TotemPopEvent(packet.getEntity(Minecraft.getMinecraft().world));
            Firework.eventBus.post(event1);
        }
    });

}

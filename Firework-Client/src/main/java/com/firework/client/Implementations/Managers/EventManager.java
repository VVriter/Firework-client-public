package com.firework.client.Implementations.Managers;

import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Client.F3Injection;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Chat.ChatReceiveE;
import com.firework.client.Implementations.Events.Chat.ChatSendE;
import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.Entity.LivingUpdateEvent;
import com.firework.client.Implementations.Events.OnFishingEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.PlayerRespawnEvent;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import com.firework.client.Implementations.Events.Render.ForgeNameTagEvent;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Events.Render.RenderGameOverlay;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;
import xyz.firework.autentification.AntiDump;

import static com.firework.client.Features.Modules.Module.mc;

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
        AntiDump.check();
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
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
        if(DiscordNotificator.serverConnection.getValue() && DiscordNotificator.enabled.getValue()){
            DiscordUtil.sendMsg("```U are disconnected from server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",DiscordNotificator.webhook);
        }
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent e){
        if(DiscordNotificator.serverConnection.getValue() && DiscordNotificator.enabled.getValue()){
            DiscordUtil.sendMsg("```U are connected to server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",DiscordNotificator.webhook);
        }
    }

    @Subscribe
    public Listener<PacketEvent.Receive> onPopEvent = new Listener<>(event-> {
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 35 && ((SPacketEntityStatus) event.getPacket()).getEntity(Minecraft.getMinecraft().world) instanceof EntityPlayer) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            TotemPopEvent event1 = new TotemPopEvent(packet.getEntity(Minecraft.getMinecraft().world));
            Firework.eventBus.post(event1);
        }
    });

    @SubscribeEvent
    public void renderNameTagsPre(RenderLivingEvent.Specials.Pre e) {
        ForgeNameTagEvent event = new ForgeNameTagEvent();
        Firework.eventBus.post(event);
        if (event.isCancelled()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event){
        Firework.eventBus.post(new LivingUpdateEvent(event.getEntityLiving()));
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event){
        Firework.eventBus.post(new PlayerRespawnEvent(event.player));
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.gameSettings.showDebugInfo && F3Injection.enabled.getValue()) {
            for (int i = 0; i < event.getLeft().size(); i++) {
                if (F3Injection.Coords.getValue()) {
                    if (event.getLeft().get(i).contains("Looking"))
                        event.getLeft().set(i, "Looking at a block!");
                    if (event.getLeft().get(i).contains("XYZ") && F3Injection.fpsmode.getValue().equals("Fake")){
                        event.getLeft().set(i, "XYZ: "+mc.player.getPosition().getX()/3*2+325*2/3*2+" "+mc.player.getPosition().getY()/3*2+" "+mc.player.getPosition().getZ()/3*2+325*2/3*2);}
                    if (event.getLeft().get(i).contains("XYZ") && F3Injection.fpsmode.getValue().equals("Hide")){
                        event.getLeft().set(i, "XYZ: NO!");}


                    if (event.getLeft().get(i).contains("Block:"))
                        event.getLeft().set(i, "Block: Hidden!");
                    if (event.getLeft().get(i).contains("Chunk:"))
                        event.getLeft().set(i, "Chunk: Hidden!");
                }
                if (F3Injection.FPS.getValue())
                    if (event.getLeft().get(i).contains("fps"))
                        event.getLeft().set(i, "fps: 0!");
                if (F3Injection.Direction.getValue())
                    if (event.getLeft().get(i).contains("Facing:"))
                        event.getLeft().set(i, "Facing: Hidden!");
                if (F3Injection.Biome.getValue())
                    if (event.getLeft().get(i).contains("Biome:"))
                        event.getLeft().set(i, "Biome: Hidden!");
            }
        }
    }

}

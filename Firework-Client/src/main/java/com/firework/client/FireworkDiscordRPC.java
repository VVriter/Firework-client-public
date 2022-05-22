package com.firework.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class FireworkDiscordRPC {
    private static DiscordRichPresence presence = new DiscordRichPresence();
    public FireworkDiscordRPC() {
        System.out.println("[RPC] Created!");
    }

    public static void Run() {
        System.out.println("[RPC] Running!");

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        String id = "977837246227054613";

        //handlers.ready = () -> System.out.println("[RPC] Ready!");
        lib.Discord_Initialize(id, handlers, true, null);

        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Username: "+ Minecraft.getMinecraft().getSession().getUsername();
        presence.state = "Firework Premium";
        presence.largeImageKey = "icon32";
        lib.Discord_UpdatePresence(presence);

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) { }
            }
        }, "RPC-Callback-Handler").start();
    }
    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent e){
        presence.state = "Playing on server "+Minecraft.getMinecraft().getCurrentServerData().serverIP;
    }
}

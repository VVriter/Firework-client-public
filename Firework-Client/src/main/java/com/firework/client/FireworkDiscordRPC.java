package com.firework.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.lwjgl.Sys;

public class FireworkDiscordRPC {
    public FireworkDiscordRPC() {
        System.out.println("[RPC] Created!");
    }

    public void Run() {
        System.out.println("[RPC] Running!");

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        String id = "977837246227054613";

        //handlers.ready = () -> System.out.println("[RPC] Ready!");
        lib.Discord_Initialize(id, handlers, true, null);

        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "<details>";
        presence.state = "<state>";
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
}

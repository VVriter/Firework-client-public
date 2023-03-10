package com.firework.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;

public class CustomDiscordRichPresence {
    private static club.minnced.discord.rpc.DiscordRichPresence presence;
    private static DiscordEventHandlers handlers;

    private static Thread thread;

    private static DiscordRPC lib = DiscordRPC.INSTANCE;

    public static void run() {

        System.out.println("[RPC] Running!");

        String id = "977837246227054613";

        presence = new club.minnced.discord.rpc.DiscordRichPresence();
        handlers = new DiscordEventHandlers();

        lib.Discord_Initialize(id, handlers, true, null);

        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Username: "+ Minecraft.getMinecraft().getSession().getUsername();
        presence.state = "Firework Premium";
        presence.largeImageKey = "picture";
        lib.Discord_UpdatePresence(presence);

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) { }
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        thread.stop();
        lib.Discord_Shutdown();

        presence = null;
        handlers = null;
    }

    public static void rerun() {
        run();
        stop();
        lib.Discord_UpdatePresence(presence);
    }
}

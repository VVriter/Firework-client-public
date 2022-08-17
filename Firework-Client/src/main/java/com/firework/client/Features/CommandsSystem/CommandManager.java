package com.firework.client.Features.CommandsSystem;

import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketChatMessage;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.*;

public class CommandManager extends Manager {
    private final List<Command> commands = new ArrayList<>();
    public CommandManager() {
        super(true);
        registerModules();
    }

    public static String prefix = ".";

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String message = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (message.startsWith(prefix)){
                String[] args = message.split(" ");
                String input = message.split(" ")[0].substring(1);
                for (Command command : commands) {
                    if (input.equalsIgnoreCase(command.getLabel()) || checkAliases(input, command)) {
                        event.setCancelled(true);
                        command.execute(args);
                    }
                }
                if (!event.isCancelled()) {
                    MessageUtil.sendClientMessage(ChatFormatting.RED + message + " was not found!", true);
                    event.setCancelled(true);
                }
                event.setCancelled(true);
            }
        }
    });

    public void registerModules() {
        Set<Class> classList = ClassFinder.findClasses(Command.class.getPackage().getName(), Command.class);
        classList.forEach(aClass -> {
            try {
                Command module = (Command) aClass.getConstructor().newInstance();
                commands.add(module);
            } catch (Exception e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate command " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        System.out.println("Commands initialised");

        commands.sort(Comparator.comparing(Command::getLabel));
    }
    private boolean checkAliases(String input, Command command) {
        for (String str : command.getAliases()) {
            if (input.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
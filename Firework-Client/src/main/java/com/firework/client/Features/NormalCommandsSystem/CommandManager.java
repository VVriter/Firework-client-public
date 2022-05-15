package com.firework.client.Features.NormalCommandsSystem;


import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Features.NormalCommandsSystem.Commands.TutorialCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager () {
        this.init();
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String message = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (message.startsWith(">")){
                String[] args = message.split(" ");
                String input = message.split(" ")[0].substring(1);
                for (Command command : commands) {
                    if (input.equalsIgnoreCase(command.getLabel()) || checkAliases(input, command)) {
                        event.setCanceled(true);
                        command.execute(args);
                    }
                }
                if (!event.isCanceled()) {
                    MessageUtil.sendClientMessage("Command " + message + " was not found!", true);
                    event.setCanceled(true);
                }
                event.setCanceled(true);
            }
        }
    }

    private boolean checkAliases(String input, Command command) {
        for (String str : command.getAliases()) {
            if (input.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public void init() {
        register(
                new TutorialCommand());

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register(Command... command) {
        Collections.addAll(commands, command);
    }

}
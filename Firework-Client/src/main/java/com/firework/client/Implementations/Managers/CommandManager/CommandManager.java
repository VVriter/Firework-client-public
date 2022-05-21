package com.firework.client.Implementations.Managers.CommandManager;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.Commands.*;
import com.firework.client.Features.CommandsSystem.Commands.Client.HelpCommand;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.FakePlayer;
import com.mojang.realmsclient.gui.ChatFormatting;
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

    public static String prefix = "";

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String message = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (message.startsWith(prefix)){
                String[] args = message.split(" ");
                String input = message.split(" ")[0].substring(1);
                for (Command command : commands) {
                    if (input.equalsIgnoreCase(command.getLabel()) || checkAliases(input, command)) {
                        event.setCanceled(true);
                        command.execute(args);
                    }
                }
                if (!event.isCanceled()) {
                    MessageUtil.sendClientMessage(ChatFormatting.RED + message + " was not found!", true);
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
                new TutorialCommand(),
                new CoordsCommand(),
                new WebhookCommand(),
                new FovCommand(),
                new NameMcCommand(),
                new HelpCommand(),
                new DupeCommand(),
                new GammaCommand(),
                new YawCommand(),
                new PitchCommand(),
                new VClipCommand(),
                new ImgurCommand(),
                new PrefixCommand(),
                new ClearCommand(),
                new ClearCommand(),
                new OpenDirCommand(),
                new GuiCommand(),
                new FakePlayerCommand());

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register(Command... command) {
        Collections.addAll(commands, command);
    }

}
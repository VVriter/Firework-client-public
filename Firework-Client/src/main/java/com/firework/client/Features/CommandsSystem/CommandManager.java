package com.firework.client.Features.CommandsSystem;


import com.firework.client.Features.CommandsSystem.Commands.Chat.ClearCommand;
import com.firework.client.Features.CommandsSystem.Commands.Chat.PrefixCommand;
import com.firework.client.Features.CommandsSystem.Commands.Chat.VClipCommand;
import com.firework.client.Features.CommandsSystem.Commands.Client.*;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.ImgurCommand;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.OpenDirCommand;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.SaveConfigCommand;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.WebhookCommand;
import com.firework.client.Features.CommandsSystem.Commands.FriendSys.FriendAdd;
import com.firework.client.Features.CommandsSystem.Commands.FriendSys.FriendDell;
import com.firework.client.Features.CommandsSystem.Commands.Fun.*;
import com.firework.client.Features.CommandsSystem.Commands.GameSettings.FovCommand;
import com.firework.client.Features.CommandsSystem.Commands.GameSettings.GammaCommand;
import com.firework.client.Features.CommandsSystem.Commands.PeekCommand.PeekCommand;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
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

    public static String prefix = ".";

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
                new CowDupeCommand(),
                new FriendAdd(),
                new FriendDell(),
                new BookCommand(),
                new PenisCommand(),
                new PeekCommand(),
                new CoordsCommand(),
                new WebhookCommand(),
                new FovCommand(),
                new SaveConfigCommand(),
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
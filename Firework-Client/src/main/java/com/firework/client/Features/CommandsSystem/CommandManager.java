package com.firework.client.Features.CommandsSystem;


import com.firework.client.Features.CommandsSystem.Commands.Chat.ClearCommand;
import com.firework.client.Features.CommandsSystem.Commands.Chat.PrefixCommand;
import com.firework.client.Features.CommandsSystem.Commands.Chat.VClipCommand;
import com.firework.client.Features.CommandsSystem.Commands.Client.*;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.ImgurCommand;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.OpenDirCommand;
import com.firework.client.Features.CommandsSystem.Commands.Dirs.SaveConfigCommand;
import com.firework.client.Features.CommandsSystem.Commands.FriendSys.FriendAdd;
import com.firework.client.Features.CommandsSystem.Commands.FriendSys.FriendDell;
import com.firework.client.Features.CommandsSystem.Commands.Fun.CowDupeCommand;
import com.firework.client.Features.CommandsSystem.Commands.Fun.DupeCommand;
import com.firework.client.Features.CommandsSystem.Commands.Fun.PenisCommand;
import com.firework.client.Features.CommandsSystem.Commands.Fun.TutorialCommand;
import com.firework.client.Features.CommandsSystem.Commands.GameSettings.FovCommand;
import com.firework.client.Features.CommandsSystem.Commands.GameSettings.GammaCommand;
import com.firework.client.Features.CommandsSystem.Commands.HelpCommand.Help1Command;
import com.firework.client.Features.CommandsSystem.Commands.HelpCommand.Help2Command;
import com.firework.client.Features.CommandsSystem.Commands.HelpCommand.Help3Command;
import com.firework.client.Features.CommandsSystem.Commands.HelpCommand.Help4Command;
import com.firework.client.Features.CommandsSystem.Commands.Hide.HideCommand;
import com.firework.client.Features.CommandsSystem.Commands.MuteSystem.GetList;
import com.firework.client.Features.CommandsSystem.Commands.MuteSystem.Mute;
import com.firework.client.Features.CommandsSystem.Commands.MuteSystem.UnMute;
import com.firework.client.Features.CommandsSystem.Commands.PeekCommand.PeekCommand;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.HelpCommand.HelpCommand;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.QueueCommand;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.SeenCommand;
import com.firework.client.Features.CommandsSystem.Commands.TwoBeeTwoTee.StatsCommand;
import com.firework.client.Features.CommandsSystem.Commands.WebhookCommand;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager extends Manager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        super(true);
        this.init();
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
                new WebhookCommand(),

                new PanicCommand(),

                new HideCommand(),

                new Help1Command(),
                new Help2Command(),
                new Help3Command(),
                new Help4Command(),

                new HelpCommand(),
                new QueueCommand(),
                new SeenCommand(),
                new StatsCommand(),

                new Mute(),
                new UnMute(),
                new GetList(),

                new TutorialCommand(),
                new CowDupeCommand(),
                new FriendAdd(),
                new FriendDell(),
                new BookCommand(),
                new PenisCommand(),
                new PeekCommand(),
                new CoordsCommand(),
                new FovCommand(),
                new SaveConfigCommand(),
                new NameMcCommand(),
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
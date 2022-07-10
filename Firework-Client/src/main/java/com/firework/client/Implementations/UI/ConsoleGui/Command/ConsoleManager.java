package com.firework.client.Implementations.UI.ConsoleGui.Command;

import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.UI.ConsoleGui.Command.Commands.ClearConsoleCommand;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsoleManager extends Manager {

    public ConsoleManager() {
        super(true);
    }

    private final List<CommandConsole> commands = new ArrayList<>();



    public void init() {
        register(
                new ClearConsoleCommand());

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register(CommandConsole... command) {
        Collections.addAll(commands, command);
    }
}

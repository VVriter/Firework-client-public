package com.firework.client.Features.Commands;

import com.firework.client.Features.Modules.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Command {

    public static final String PREFIX = ">";

    private String commandName;
    private Module coreModule;

    public Command(Module coreModule, String commandName)
    {
        this.coreModule = coreModule;
        this.commandName = commandName;
    }

    public void executeCoreModule() {
        coreModule.onTick();
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatEvent e) {
        String content = e.getMessage();
        if(content == (Command.PREFIX + commandName.toLowerCase())) {
            e.setCanceled(true);
            executeCoreModule();
        }
    }
}

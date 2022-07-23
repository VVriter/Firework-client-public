package com.firework.client.Features.CommandsSystem.Commands.Chat;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;

@CommandManifest(label = "clear",aliases = "cl")
public class ClearCommand extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
    }
}

package com.firework.client.Features.CommandsSystem.Commands;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "clear",aliases = "cl")
public class ClearCommand extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
    }
}

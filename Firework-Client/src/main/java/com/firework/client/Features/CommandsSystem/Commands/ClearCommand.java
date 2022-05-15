package com.firework.client.Features.CommandsSystem.Commands;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "clear",aliases = "cl")
public class ClearCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
    }
}

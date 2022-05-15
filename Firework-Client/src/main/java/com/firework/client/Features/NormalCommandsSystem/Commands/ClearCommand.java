package com.firework.client.Features.NormalCommandsSystem.Commands;


import com.firework.client.Features.NormalCommandsSystem.Command;
import com.firework.client.Features.NormalCommandsSystem.CommandManifest;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "clear",aliases = "cl")
public class ClearCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
    }
}

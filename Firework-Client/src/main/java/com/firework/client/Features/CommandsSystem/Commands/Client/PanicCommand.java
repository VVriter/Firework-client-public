package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import net.minecraft.init.MobEffects;
import org.lwjgl.opengl.Display;

import static com.firework.client.Features.Modules.Module.mc;

@CommandManifest(label = "panic")
public class PanicCommand extends Command {
    @Override
    public void execute(String[] args) {
        Firework.minecraft.ingameGUI.getChatGUI().clearChatMessages(true);
        Display.setTitle("Minecraft 1.12.2");
        Firework.unloadManagers();
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        mc.gameSettings.gammaSetting = 0;
    }
}
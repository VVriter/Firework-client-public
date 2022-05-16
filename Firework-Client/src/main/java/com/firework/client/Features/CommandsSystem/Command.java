package com.firework.client.Features.CommandsSystem;

import com.firework.client.Implementations.Managers.CommandManager.CommandManager;
import net.minecraft.client.Minecraft;

public class Command  {

    String label;
    String[] aliases;
    public static String prefix = CommandManager.prefix;

    public Command() {
        if (getClass().isAnnotationPresent(CommandManifest.class)) {
            CommandManifest manifest = getClass().getAnnotation(CommandManifest.class);
            label = manifest.label();
            aliases = manifest.aliases();
        }
    }

    public void execute(String[] args) {}

    public String getLabel() {
        return label;
    }

    public String[] getAliases() {
        return aliases;
    }
}
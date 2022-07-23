package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import net.minecraft.client.renderer.GlStateManager;

@CommandManifest(label = "scale")
public class ScaleCommand extends Command {
    @Override
    public void execute(String[] args) {
        super.execute(args);

        float scale = Float.parseFloat(args[0]);

        GlStateManager.scale(scale, scale, scale);
    }
}

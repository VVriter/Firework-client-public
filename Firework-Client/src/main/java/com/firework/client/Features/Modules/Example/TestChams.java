package com.firework.client.Features.Modules.Example;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@ModuleManifest(
        name = "TestChams",
        category = Module.Category.EXAMPLE
)
public class TestChams extends Module {
    ArrayList<Entity> players = new ArrayList<>();
    @Override
    public void onUpdate() { super.onUpdate();
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                players.add(entity);
            }
        }
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (Entity entity : players) {
            if (entity != null) {
                RenderUtils.drawProperBox(entity.getPosition(), Color.BLUE);
            }
        }
    }
}

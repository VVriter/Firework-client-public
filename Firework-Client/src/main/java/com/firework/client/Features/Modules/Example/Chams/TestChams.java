package com.firework.client.Features.Modules.Example.Chams;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonOffset;

@ModuleManifest(
        name = "TestChams",
        category = Module.Category.EXAMPLE
)
public class TestChams extends Module {
    public static Setting<Boolean> lLighting = null;
    public static Setting<Boolean> lFillDepth = null;
    public TestChams() {
        lLighting = new Setting<>("Lighting", true, this);
        lFillDepth = new Setting<>("FillDepth", true, this);
    }

    ModelBase modelBase;

    @Override public void onEnable() { super.onEnable();
        players.clear();
    }

    @Override public void onDisable() { super.onDisable();
        players.clear();
    }



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

            }
        }
    }
}

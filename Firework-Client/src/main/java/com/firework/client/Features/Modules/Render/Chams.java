package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@ModuleManifest(name = "Chams", category = Module.Category.RENDER)
public class Chams extends Module{
    private Setting<modes> mode = new Setting<>("Mode", modes.Shader, this, modes.values());
    private enum modes{
        Shader, Outline
    }

    //----------Shader chams settings----------//
    private ArrayList<String> shadersList = Firework.shaderManager.getShadersNames();
    //Shader chams mode
    private Setting<String> shader = new Setting<>("Shader", shadersList.get(0),this, shadersList);

    @SubscribeEvent
    public void renderEntities(RenderWorldLastEvent event){
        if(mode.getValue(modes.Shader)) {
            if (Firework.shaderManager.getShaderByName(shader.getValue()) == null) return;
            GlStateManager.pushMatrix();
            Firework.shaderManager.getShaderByName(shader.getValue()).startDraw(event.getPartialTicks());
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity == mc.player && entity == mc.getRenderViewEntity()) continue;
                Vec3d vector = EntityUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
                mc.getRenderManager().getEntityRenderObject(entity).doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
            }
            Firework.shaderManager.getShaderByName(shader.getValue()).stopDraw();
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.popMatrix();
        }
    }
}

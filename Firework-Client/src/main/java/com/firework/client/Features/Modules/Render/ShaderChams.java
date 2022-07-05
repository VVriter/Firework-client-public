package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@ModuleManifest(name = "ShaderChams", category = Module.Category.RENDER)
public class ShaderChams extends Module{
    public ArrayList<String> shadersList = Firework.shaderManager.getShadersNames();
    public Setting<String> shader = new Setting<>("Shader", shadersList.get(0),this, shadersList);

    @SubscribeEvent
    public void renderEntities(RenderWorldLastEvent event){
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        Firework.shaderManager.getShaderByName(shader.getValue()).startDraw(event.getPartialTicks());
        for(Entity entity : mc.world.loadedEntityList) {
            mc.getRenderManager().getEntityRenderObject(entity).doRender(entity, entity.posX, entity.posY, entity.posZ, entity.rotationYaw, event.getPartialTicks());
        }
        Firework.shaderManager.getShaderByName(shader.getValue()).stopDraw();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
    }
}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@ModuleManifest(
        name = "EntityViewRenderer",
        category = Module.Category.RENDER
)
public class EyeFinder extends Module {

    PosRenderer posRenderer;
    public Setting<Enum> page = new Setting<>("Page", pages.Line, this, pages.values());
    public enum pages{
        Line, Block
    }
    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("RenderMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values()).setVisibility(v-> page.getValue(pages.Block));
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this, PosRenderer.boxeMode.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("OutlineMode", PosRenderer.outlineModes.Normal, this, PosRenderer.outlineModes.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && page.getValue(pages.Block));
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Block));
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Block));
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Block));
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Block));
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && !outlineMode.getValue(PosRenderer.outlineModes.None) && page.getValue(pages.Block));

    public Setting<Double> distance = new Setting<>("Distance", (double)15, this, 1, 50).setVisibility(v-> page.getValue(pages.Line));
    public Setting<Double> lineDistance = new Setting<>("LineDistance", (double)6, this, 1, 15).setVisibility(v-> page.getValue(pages.Line));
    public Setting<Double> eyeLineWidth = new Setting<>("LineWidth", (double)3, this, 1, 10).setVisibility(v-> page.getValue(pages.Line));
    public Setting<HSLColor> viewLineColor = new Setting<>("ViewLineColor", new HSLColor(1, 54, 43), this).setVisibility(v-> page.getValue(pages.Line));
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        mc.world.loadedEntityList.stream().filter(entity -> mc.player != entity && !entity.isDead && entity instanceof EntityPlayer && mc.player.getDistance(entity) <= distance.getValue()).forEach(this::drawLine);
    }



    private void drawLine(final Entity e) {
        final RayTraceResult result = e.rayTrace(lineDistance.getValue(), mc.getRenderPartialTicks());
        if (result == null) return;
        final Vec3d eyes = e.getPositionEyes(mc.getRenderPartialTicks());
        GL11.glPushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        final double posX = eyes.x - mc.getRenderManager().viewerPosX;
        final double posY = eyes.y - mc.getRenderManager().viewerPosY;
        final double posZ = eyes.z - mc.getRenderManager().viewerPosZ;
        final double posX2 = result.hitVec.x - mc.getRenderManager().viewerPosX;
        final double posY2 = result.hitVec.y - mc.getRenderManager().viewerPosY;
        final double posZ2 = result.hitVec.z - mc.getRenderManager().viewerPosZ;
        //Colour
        GlStateManager.glLineWidth(eyeLineWidth.getValue().floatValue());
        GL11.glBegin(1);
        GL11.glColor4d(viewLineColor.getValue().toRGB().getRed(),viewLineColor.getValue().toRGB().getGreen(), viewLineColor.getValue().toRGB().getBlue(), viewLineColor.getValue().toRGB().getAlpha());
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glEnd();
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                posRenderer.doRender(
                        result.getBlockPos(),
                        colorOutline.getValue().toRGB(),
                        gradientOutlineColor1.getValue().toRGB(),
                        gradientOutlineColor2.getValue().toRGB(),
                        fillColor.getValue().toRGB(),
                        fillColor1.getValue().toRGB(),
                        fillColor2.getValue().toRGB(),
                        outlineWidth.getValue(),
                        boxHeightNormal.getValue().floatValue(),
                        outlineHeightNormal.getValue().floatValue()
                );
            }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }


    @Override
    public void onEnable(){
        super.onEnable();
        posRenderer = new PosRenderer(this,renderMode,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }
}

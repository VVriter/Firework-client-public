package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleManifest(
        name = "EntityViewRenderer",
        category = Module.Category.VISUALS
)
public class EyeFinder extends Module {

    PosRenderer posRenderer;
    public Setting<Enum> page = new Setting<>("Page", pages.Line, this);
    public enum pages{
        Line, Block
    }
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this).setVisibility(v-> page.getValue(pages.Block));
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) &&  page.getValue(pages.Block));
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) &&  page.getValue(pages.Block));
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  page.getValue(pages.Block));
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  page.getValue(pages.Block));



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("OutlineMode", PosRenderer.outlineModes.Normal, this).setVisibility(v->  page.getValue(pages.Block));
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Block));
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && page.getValue(pages.Block));
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Block));
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && page.getValue(pages.Block));
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v->  !outlineMode.getValue(PosRenderer.outlineModes.None) && page.getValue(pages.Block));

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

        drawLine(eyes, result.hitVec, eyeLineWidth.getValue().floatValue(), viewLineColor.getValue().toRGB());

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
    }

    public static void drawLine(Vec3d posA, Vec3d posB, float width, Color c) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        double dx = posB.x - posA.x;
        double dy = posB.y - posA.y;
        double dz = posB.z - posA.z;

        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        tessellator.draw();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }


    @Override
    public void onEnable(){
        super.onEnable();
        posRenderer = new PosRenderer(this,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }
}

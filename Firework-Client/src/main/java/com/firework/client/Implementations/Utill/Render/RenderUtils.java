package com.firework.client.Implementations.Utill.Render;

import com.firework.client.Implementations.Utill.Blocks.BoundingBoxUtil;
import com.firework.client.Implementations.Utill.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.*;
import java.awt.*;
import net.minecraft.world.*;
import net.minecraft.block.state.*;
import java.util.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static ICamera camera = new Frustum();
    static {
        camera = new Frustum();
    }

    static public float colorcalc(int c, int location) {
        return (c >> location & 0xFF) / 255.0F;
    }

    public static void renderEntity(EntityLivingBase entity, int scale, int posX, int posY) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPopAttrib();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.color(1,1,1,1);
        GuiInventory.drawEntityOnScreen(posX, posY,scale,1,1,entity);
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
    }

    public static void blockESP(BlockPos blockPos) {
        GL11.glPushMatrix();

        double x =
                blockPos.getX()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double y =
                blockPos.getY()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double z =
                blockPos.getZ()
                        - Minecraft.getMinecraft().getRenderManager().viewerPosZ;

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glDepthMask(false);

        RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1, 0, 0, 0.5F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glDepthMask(true);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawLine(Vec3d point1, Vec3d point2, float width, Color c) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        bufferBuilder.pos(point1.x, point1.y, point1.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(point2.x, point2.y, point2.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

        tessellator.draw();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawSphere(Vec3d vec3d, double radius, int lineWidth, Color color){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(lineWidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * radius;
            double y2 = Math.cos(((i * Math.PI) / 180)) * radius;

            Vec3d vec3d1 = vec3d.add(x2, y2, 0);
            double r = vec3d1.distanceTo(vec3d.add(0, y2, 0));
            for (int u = 0; u <= 360; u++)
            {
                double x3 = Math.sin(((u * Math.PI) / 180)) * r;
                double z3 = Math.cos(((u * Math.PI) / 180)) * r;

                bufferBuilder.pos(vec3d.x + x3, vec3d.y + y2, vec3d.z + z3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            }
        }

        tessellator.draw();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawCircle(Vec3d vec3d, double radius, Color color){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i <= 360; i++)
        {
            double x2 = Math.sin(((i * Math.PI) / 180)) * radius;
            double z2 = Math.cos(((i * Math.PI) / 180)) * radius;

            bufferBuilder.pos(vec3d.x + x2, vec3d.y, vec3d.z + z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        }

        tessellator.draw();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }









    //-----------------------------------------------------------------------------------------------------------------------
    public static final Tessellator tessellator;
    private static boolean depth;
    private static boolean texture;
    private static boolean clean;
    private static boolean bind;
    private static boolean override;

    public static void GlPost() {
        GLPost(RenderUtils.depth, RenderUtils.texture, RenderUtils.clean, RenderUtils.bind, RenderUtils.override);
    }

    private static void GLPost(final boolean depth, final boolean texture, final boolean clean, final boolean bind, final boolean override) {
        GlStateManager.depthMask(true);
        if (!override) {
            GL11.glDisable(2848);
        }
        if (bind) {
            GL11.glEnable(2929);
        }
        if (clean) {
            GL11.glEnable(3553);
        }
        if (!texture) {
            GL11.glDisable(3042);
        }
        if (depth) {
            GL11.glEnable(2896);
        }
    }

    public static void GLPre(final float lineWidth) {
        RenderUtils.depth = GL11.glIsEnabled(2896);
        RenderUtils.texture = GL11.glIsEnabled(3042);
        RenderUtils.clean = GL11.glIsEnabled(3553);
        RenderUtils.bind = GL11.glIsEnabled(2929);
        RenderUtils.override = GL11.glIsEnabled(2848);
        GLPre(RenderUtils.depth, RenderUtils.texture, RenderUtils.clean, RenderUtils.bind, RenderUtils.override, lineWidth);
    }

    private static void GLPre(final boolean depth, final boolean texture, final boolean clean, final boolean bind, final boolean override, final float lineWidth) {
        if (depth) {
            GL11.glDisable(2896);
        }
        if (!texture) {
            GL11.glEnable(3042);
        }
        GL11.glLineWidth(lineWidth);
        if (clean) {
            GL11.glDisable(3553);
        }
        if (bind) {
            GL11.glDisable(2929);
        }
        if (!override) {
            GL11.glEnable(2848);
        }
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint(3154, 4354);
        GlStateManager.depthMask(false);
    }



    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        final int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = (distance >> 1) / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }

    public static void drawProperOutline(final BlockPos pos, final Color color) {
        final IBlockState iblockstate = RenderUtils.mc.world.getBlockState(pos);
        final Entity player = RenderUtils.mc.getRenderViewEntity();
        final double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * RenderUtils.mc.getRenderPartialTicks();
        final double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * RenderUtils.mc.getRenderPartialTicks();
        final double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * RenderUtils.mc.getRenderPartialTicks();
        RenderGlobal.drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox((World)RenderUtils.mc.world, pos).grow(0.0020000000949949026).offset(-d3, -d4, -d5), color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public static void renderProperOutline(final BlockPos pos, final Color color, final float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);
        drawProperOutline(pos, color);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawProperBox(final BlockPos pos, final Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() - RenderUtils.mc.getRenderManager().viewerPosZ, pos.getX() + 1 - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() + 1 - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() + 1 - RenderUtils.mc.getRenderManager().viewerPosZ);
        RenderUtils.camera.setPosition(Objects.requireNonNull(RenderUtils.mc.getRenderViewEntity()).posX, RenderUtils.mc.getRenderViewEntity().posY, RenderUtils.mc.getRenderViewEntity().posZ);
        if(RenderUtils.camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawProperBox(final BlockPos pos, final Color color, final int alpha) {
        final IBlockState iblockstate = RenderUtils.mc.world.getBlockState(pos);
        final Entity player = RenderUtils.mc.getRenderViewEntity();
        final double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * RenderUtils.mc.getRenderPartialTicks();
        final double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * RenderUtils.mc.getRenderPartialTicks();
        final double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * RenderUtils.mc.getRenderPartialTicks();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderGlobal.renderFilledBox(iblockstate.getSelectedBoundingBox((World)RenderUtils.mc.world, pos).grow(0.002).offset(-d3, -d4, -d5), color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }


    public static void drawBoundingBox(final AxisAlignedBB bb, final Color color, final float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);
        RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawProperBoxESP(final BlockPos pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final float height) {
        RenderUtils.camera.setPosition(Objects.requireNonNull(RenderUtils.mc.getRenderViewEntity()).posX, RenderUtils.mc.getRenderViewEntity().posY, RenderUtils.mc.getRenderViewEntity().posZ);
        if (RenderUtils.camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            if (box) {
                drawProperBox(pos, color, boxAlpha);
            }
            if (outline) {
                drawProperOutline(pos, color);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBoxESP(final BlockPos pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final float height) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() - RenderUtils.mc.getRenderManager().viewerPosZ, pos.getX() + 1 - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() + height - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() + 1 - RenderUtils.mc.getRenderManager().viewerPosZ);
        RenderUtils.camera.setPosition(Objects.requireNonNull(RenderUtils.mc.getRenderViewEntity()).posX, RenderUtils.mc.getRenderViewEntity().posY, RenderUtils.mc.getRenderViewEntity().posZ);
        //if (RenderUtils.camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            if (box) {
                RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, boxAlpha / 255.0f);
            }
            if (outline) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        //}
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth) {
        AxisAlignedBB bb = BoundingBoxUtil.getBB(pos);
        float red = (float) startColor.getRed() / 255.0f;
        float green = (float) startColor.getGreen() / 255.0f;
        float blue = (float) startColor.getBlue() / 255.0f;
        float alpha = (float) startColor.getAlpha() / 255.0f;
        float red1 = (float) endColor.getRed() / 255.0f;
        float green1 = (float) endColor.getGreen() / 255.0f;
        float blue1 = (float) endColor.getBlue() / 255.0f;
        float alpha1 = (float) endColor.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientBlockOutlineWithHeight(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
        float red = (float) startColor.getRed() / 255.0f;
        float green = (float) startColor.getGreen() / 255.0f;
        float blue = (float) startColor.getBlue() / 255.0f;
        float alpha = (float) startColor.getAlpha() / 255.0f;
        float red1 = (float) endColor.getRed() / 255.0f;
        float green1 = (float) endColor.getGreen() / 255.0f;
        float blue1 = (float) endColor.getBlue() / 255.0f;
        float alpha1 = (float) endColor.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientFilledBox(BlockPos pos, Color startColor, Color endColor) {
        AxisAlignedBB bb = BoundingBoxUtil.getBB(pos);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        float alpha = (float) endColor.getAlpha() / 255.0f;
        float red = (float) endColor.getRed() / 255.0f;
        float green = (float) endColor.getGreen() / 255.0f;
        float blue = (float) endColor.getBlue() / 255.0f;
        float alpha1 = (float) startColor.getAlpha() / 255.0f;
        float red1 = (float) startColor.getRed() / 255.0f;
        float green1 = (float) startColor.getGreen() / 255.0f;
        float blue1 = (float) startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }


    public static void drawGradientFilledBoxWithHeight(AxisAlignedBB bb, Color startColor, Color endColor) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        float alpha = (float) endColor.getAlpha() / 255.0f;
        float red = (float) endColor.getRed() / 255.0f;
        float green = (float) endColor.getGreen() / 255.0f;
        float blue = (float) endColor.getBlue() / 255.0f;
        float alpha1 = (float) startColor.getAlpha() / 255.0f;
        float red1 = (float) startColor.getRed() / 255.0f;
        float green1 = (float) startColor.getGreen() / 255.0f;
        float blue1 = (float) startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void renderCrosses(final AxisAlignedBB bb, final Color color) {
        final int hex = color.getRGB();
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        final BufferBuilder bufferbuilder = RenderUtils.tessellator.getBuffer();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, 1.0f).endVertex();
        RenderUtils.tessellator.draw();
    }

    public static void renderCrosses(final BlockPos pos, final Color color, final float lineWidth) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() - RenderUtils.mc.getRenderManager().viewerPosZ, pos.getX() + 1 - RenderUtils.mc.getRenderManager().viewerPosX, pos.getY() + 1 - RenderUtils.mc.getRenderManager().viewerPosY, pos.getZ() + 1 - RenderUtils.mc.getRenderManager().viewerPosZ);
        RenderUtils.camera.setPosition(Objects.requireNonNull(RenderUtils.mc.getRenderViewEntity()).posX, RenderUtils.mc.getRenderViewEntity().posY, RenderUtils.mc.getRenderViewEntity().posZ);
        if (RenderUtils.camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            renderCrosses(bb, color);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    static Set<Vec3d> buildPoints(Vec3d center, double radius) {
        Set<Vec3d> points = new HashSet<>(1200);

        double tau = 6.283185307179586D;
        double pi = tau / 2D;
        double segment = tau / 48D;

        for (double t = 0.0D; t < tau; t += segment)
            for (double theta = 0.0D; theta < pi; theta += segment) {
                double dx = radius * Math.sin(t) * Math.cos(theta);
                double dz = radius * Math.sin(t) * Math.sin(theta);
                double dy = radius * Math.cos(t);
            }
        return points;
    }
    static {
        camera = (ICamera)new Frustum();
        tessellator = Tessellator.getInstance();
        RenderUtils.depth = GL11.glIsEnabled(2896);
        RenderUtils.texture = GL11.glIsEnabled(3042);
        RenderUtils.clean = GL11.glIsEnabled(3553);
        RenderUtils.bind = GL11.glIsEnabled(2929);
        RenderUtils.override = GL11.glIsEnabled(2848);
    }

    public static void fakeGuiRect(double left, double top, double right, double bottom, int color) {
        double f3;

        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        float f31 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f31);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFreecamESP(double x1, double y1, double z1, int red, int green, int blue) {
        try {
            double renderPosX = Globals.MC.getRenderManager().viewerPosX;
            double renderPosY = Globals.MC.getRenderManager().viewerPosY;
            double renderPosZ = Globals.MC.getRenderManager().viewerPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)3553);
            GL11.glLineWidth((float)2.0f);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4d((double)red, (double)green, (double)blue, (double)0.1825f);
            double x = x1 - renderPosX;
            double y = y1 - renderPosY;
            double z = z1 - renderPosZ;
            RenderUtils.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 0.6, y + 1.8, z + 0.6));
            GL11.glColor4d((double)red, (double)green, (double)blue, (double)1.0);
            RenderUtils.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 0.6, y + 1.8, z + 0.6));
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2896);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }


    public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public static void drawRect(final Rectangle rectangle, final int color) {
        drawRect((float)rectangle.x, (float)rectangle.y, (float)(rectangle.x + rectangle.width), (float)(rectangle.y + rectangle.height), color);
    }

    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = RenderUtils.tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos((double)x, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        RenderUtils.tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(final float x, final float y, final float x1, final float y1) {
        final BufferBuilder builder = RenderUtils.tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos((double)x, (double)y1, 0.0).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).endVertex();
        builder.pos((double)x1, (double)y, 0.0).endVertex();
        builder.pos((double)x, (double)y, 0.0).endVertex();
        RenderUtils.tessellator.draw();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void drawOutlineRect(final float x, final float y, final float w, final float h, final float lineWidth, final int c) {
        drawRect(x, y, x - lineWidth, h, c);
        drawRect(w + lineWidth, y, w, h, c);
        drawRect(x, y, w, y - lineWidth, c);
        drawRect(x, h + lineWidth, w, h, c);
    }

    public static void disableGL2D(final boolean unused) {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void enableGL2D(final boolean unused) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color, final int ignored) {
        enableGL2D(false);
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        disableGL2D(false);
    }

    public static void enableGL2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void disableGL2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, final int insideC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, new Color(RainbowUtil.getRainbow(5000, 0, 1.0f)).getRGB());
        drawVLine(x1 - 1.0f, y, y1, new Color(RainbowUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        drawGradientHLine(x, x1 - 1.0f, y, new Color(RainbowUtil.getRainbow(5000, 0, 1.0f)).getRGB(), new Color(RainbowUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        drawGradientHLine(x, x1 - 2.0f, y1 - 1.0f, new Color(RainbowUtil.getRainbow(5000, 0, 1.0f)).getRGB(), new Color(RainbowUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void drawGradientHRect(final float x, final float y, final float x1, final float y1, final int topColor, final int bottomColor) {
        final float alpha = (topColor >> 24 & 0xFF) / 255.0f;
        final float red = (topColor >> 16 & 0xFF) / 255.0f;
        final float green = (topColor >> 8 & 0xFF) / 255.0f;
        final float blue = (topColor & 0xFF) / 255.0f;
        final float alpha2 = (bottomColor >> 24 & 0xFF) / 255.0f;
        final float red2 = (bottomColor >> 16 & 0xFF) / 255.0f;
        final float green2 = (bottomColor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (bottomColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final BufferBuilder builder = RenderUtils.tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos((double)x1, (double)y1, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos((double)x1, (double)y, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        RenderUtils.tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


  public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawGradientHLine(float x, float y, final float x1, final int color1, final int color2) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawGradientHRect(x, x1, y + 1.0f, x1 + 1.0f, color1, color2);
    }

    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }


}
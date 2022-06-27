package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import static com.firework.client.Implementations.Utill.Util.*;
import static com.firework.client.Implementations.Utill.Render.RenderUtils.*;

/*
    @author PunCakeCat
 */
public class BlockRenderBuilder {

    private BlockPos blockPos;
    private ArrayList<RenderMode> modes;

    public BlockRenderBuilder(BlockPos blockPos){
        this.blockPos = blockPos;
        this.modes = new ArrayList<>();
    }

    public BlockRenderBuilder addRenderMode(RenderMode mode){
        modes.add(mode);
        return this;
    }

    public BlockRenderBuilder render(){
        drawESP(blockPos);
        return this;
    }

    public RenderMode getRenderMode(RenderMode.renderModes mode){
        for(RenderMode renderMode : modes)
            if(renderMode.mode == mode)
                return renderMode;

        return null;
    }

    public void drawESP(final BlockPos pos) {
        RenderUtils.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (RenderUtils.camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            if (getRenderMode(RenderMode.renderModes.Fill) != null) {
                final Color color = (Color) getRenderMode(RenderMode.renderModes.Fill).values.get(0);
                drawProperBox(pos, color, color.getAlpha());
            }
            if (getRenderMode(RenderMode.renderModes.OutLine) != null) {
                GL11.glLineWidth((Float) getRenderMode(RenderMode.renderModes.OutLine).values.get(1));
                final Color color = (Color) getRenderMode(RenderMode.renderModes.OutLine).values.get(0);
                drawProperOutline(pos, color);
            }
            if (getRenderMode(RenderMode.renderModes.Gradient) != null) {
                final Color color1 = (Color) getRenderMode(RenderMode.renderModes.Gradient).values.get(0);
                final Color color2 = (Color) getRenderMode(RenderMode.renderModes.Gradient).values.get(1);
                drawProperGradientBox(pos, color1, color2);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}

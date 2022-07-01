package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import com.firework.client.Implementations.Utill.Blocks.BoundingBoxUtil;
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

    public BlockRenderBuilder addRenderModes(RenderMode... modes){
        for(RenderMode mode : modes){
            this.modes.add(mode);
        }
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
            if (getRenderMode(RenderMode.renderModes.OutLine) != null) {
                GL11.glLineWidth((Float) getRenderMode(RenderMode.renderModes.OutLine).values.get(1));
                final Color color = (Color) getRenderMode(RenderMode.renderModes.OutLine).values.get(0);
                drawProperOutline(pos, color);
            }
            if (getRenderMode(RenderMode.renderModes.Fill) != null) {
                final Color color = (Color) getRenderMode(RenderMode.renderModes.Fill).values.get(0);
                drawProperBox(pos, color, color.getAlpha());
            }
            if (getRenderMode(RenderMode.renderModes.OutlineGradient) != null) {
                final Color color1 = (Color) getRenderMode(RenderMode.renderModes.OutlineGradient).values.get(0);
                final Color color2 = (Color) getRenderMode(RenderMode.renderModes.OutlineGradient).values.get(1);
                final float lineWidth = (Float) getRenderMode(RenderMode.renderModes.OutlineGradient).values.get(2);
                drawGradientBlockOutline(pos, color1, color2, lineWidth);
            }
            if (getRenderMode(RenderMode.renderModes.FilledGradient) != null) {
                final Color color1 = (Color) getRenderMode(RenderMode.renderModes.FilledGradient).values.get(0);
                final Color color2 = (Color) getRenderMode(RenderMode.renderModes.FilledGradient).values.get(1);
                drawGradientFilledBox(pos, color1, color2);
            }
        }
    }
}

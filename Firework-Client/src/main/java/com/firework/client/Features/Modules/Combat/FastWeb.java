package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Example.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "FastWeb",
        category = Module.Category.COMBAT
)
public class FastWeb extends Module {
    public Setting<Enum> page = new Setting<>("Page", pages.FastWeb, this, pages.values());
    public enum pages{
        FastWeb, Render
    }
    public Setting<modes> mode = new Setting<>("Mode", modes.Motion, this, modes.values()).setVisibility(v-> page.getValue(pages.FastWeb));
    public enum modes {
        Timer, Motion
    }
    public Setting<Double> reduction = new Setting<>("Reduction", (double)0.3, this, 0, 2).setVisibility(v-> mode.getValue(modes.Motion) && page.getValue(pages.FastWeb));
    public Setting<Double> ticks = new Setting<>("Reduction Ticks", (double)45, this, 40, 60).setVisibility(v-> mode.getValue(modes.Timer) && page.getValue(pages.FastWeb));

    public Setting<Boolean> render = new Setting<>("Render", false, this).setVisibility(v-> page.getValue(pages.Render));
    public Setting<Double> range = new Setting<>("RenderRange", (double)10, this, 1, 20).setVisibility(v-> render.getValue(true) && page.getValue(pages.Render));
    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("RenderMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values()).setVisibility(v-> render.getValue() && page.getValue(pages.Render));
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("Box", PosRenderer.boxeMode.Normal, this, PosRenderer.boxeMode.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));

    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("Outline", PosRenderer.outlineModes.Normal, this, PosRenderer.outlineModes.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue() && page.getValue(pages.Render));
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue() && page.getValue(pages.Render));
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue() && page.getValue(pages.Render));
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && !outlineMode.getValue(PosRenderer.outlineModes.None) && render.getValue() && page.getValue(pages.Render));
    PosRenderer posRenderer;
    @Override public void onEnable() { super.onEnable();
        oldTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        posRenderer = new PosRenderer(this,renderMode,boxMode,outlineMode);
    }

    @Override public void onDisable() { super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
        posRenderer = null;
    }

    @Override
    public void onTick() {
        super.onTick();
        if (mode.getValue(modes.Motion)) {
        if (((IEntity)mc.player).isInWeb()) {
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                     pushDown();
                }
            }
        }
        if (!((IEntity)mc.player).isInWeb() && ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength() != oldTicks ) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (mode.getValue(modes.Timer)) {
        if (((IEntity)mc.player).isInWeb() && mode.getValue(modes.Timer)) {
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.onGround) {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks-ticks.getValue().floatValue());
            } else {
                ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
                }
            }
        }
    }
    float oldTicks;
    void pushDown() {
        mc.player.motionY = -reduction.getValue();
    }

    public List<BlockPos> calcPoses() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (BlockUtil.getBlock(pos) == Blocks.WEB) {
                safeSpots.add(pos);
            }
        }
        return safeSpots;
    }

    //Render
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (render.getValue()) {
        for (BlockPos poses : calcPoses()) {
            if (posRenderer != null && poses != null) {
                posRenderer.doRender(
                        poses,
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
        }
    }
}

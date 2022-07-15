package com.firework.client.Features.Modules.Example;

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
public class FastWebBeta extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Motion, this, modes.values());
    public enum modes {
        Timer, Motion
    }
    public Setting<Double> reduction = new Setting<>("Reduction", (double)0.3, this, 0, 2).setVisibility(mode,modes.Motion);
    public Setting<Double> ticks = new Setting<>("Reduction", (double)45, this, 40, 60).setVisibility(mode,modes.Timer);;

    public Setting<Boolean> render = new Setting<>("Render", false, this);
    public Setting<Double> range = new Setting<>("RenderRange", (double)10, this, 1, 20).setVisibility(render,true);
    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("SwitchMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values()).setVisibility(render,true);
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this).setVisibility(render,true);
    public Setting<HSLColor> gradCo1 = new Setting<>("Color1", new HSLColor(1, 54, 43), this).setVisibility(render,true);
    public Setting<HSLColor> gradCo2 = new Setting<>("Color2", new HSLColor(200, 54, 43), this).setVisibility(render,true);
    public Setting<Double> width = new Setting<>("Width", (double)3, this, 1, 10).setVisibility(render,true);

    PosRenderer posRenderer;
    @Override public void onEnable() { super.onEnable();
        oldTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        posRenderer = new PosRenderer(this,renderMode);
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
                        colorSetting.getValue().toRGB(),
                        width.getValue().floatValue(),
                        gradCo1.getValue().toRGB(),
                        gradCo2.getValue().toRGB()
                        );
                    }
            }
        }
    }
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Mixins.MixinsList.IEntity;
import com.firework.client.Implementations.Mixins.MixinsList.IMinecraft;
import com.firework.client.Implementations.Mixins.MixinsList.ITimer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Math.Inhibitator;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "FastWeb",
        category = Module.Category.COMBAT,
        description = "Prevents CobWeb slowness"
)
public class FastWeb extends Module {
    public Setting<modes> mode = new Setting<>("Mode", modes.Timer, this);
    public enum modes {
        Timer, Motion
    }
    public Setting<Double> reduction = new Setting<>("Reduction", (double)0.3, this, 0, 2).setVisibility(v-> mode.getValue(modes.Motion));
    public Setting<Double> ticks = new Setting<>("Reduction Ticks", (double)45, this, 40, 49).setVisibility(v-> mode.getValue(modes.Timer));

    public Setting<Boolean> inhibit = new Setting<>("Inhibit", false, this).setMode(Setting.Mode.SUB).setVisibility(v-> mode.getValue(modes.Timer));
    public Setting<Boolean> inhibitBool = new Setting<>("InhibitEnable", true, this).setVisibility(v-> inhibit.getValue() && mode.getValue(modes.Timer));
    public Setting<Double> inhibitationSpeed = new Setting<>("InhibitDelay", (double)50, this, 1, 200).setVisibility(v-> inhibit.getValue() && mode.getValue(modes.Timer));
    Inhibitator inhibitator = new Inhibitator();

    @Override
    public void onToggle() {
        super.onToggle();
        inhibitator.timer.reset();
    }

    float oldTicks;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (inhibitBool.getValue() && mode.getValue(modes.Timer)) {
            inhibitator.doInhibitation(ticks,inhibitationSpeed.getValue(),49,40);
        }


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
        if (mode.getValue(modes.Timer)) {
            if (((IEntity)mc.player).isInWeb() && mode.getValue(modes.Timer)) {
                if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.onGround) {
                    ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks-ticks.getValue().floatValue());
                } else {
                    ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
                }
            }
        }
    });
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

    public Setting<Boolean> renderer = new Setting<>("Block", false, this).setMode(Setting.Mode.SUB);
    public Setting<Double> range = new Setting<>("RenderRange", (double)10, this, 1, 20).setVisibility(v-> renderer.getValue());
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("Box", PosRenderer.boxeMode.Normal, this).setVisibility(v->  renderer.getValue());
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderer.getValue());
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderer.getValue());
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  renderer.getValue());
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  renderer.getValue());

    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("Outline", PosRenderer.outlineModes.Normal, this).setVisibility(v-> renderer.getValue());
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && renderer.getValue());
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && renderer.getValue());
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && renderer.getValue());
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> outlineMode.getValue(PosRenderer.outlineModes.Normal) && renderer.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> !outlineMode.getValue(PosRenderer.outlineModes.None) && renderer.getValue());

    PosRenderer posRenderer;
    @Subscribe
    public Listener<Render3dE> listener2 = new Listener<>(event -> {
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
    });

    @Override public void onEnable() { super.onEnable();
        oldTicks = ((ITimer) ((IMinecraft) mc).getTimer()).getTickLength();
        posRenderer = new PosRenderer(this,boxMode,outlineMode);
    }

    @Override public void onDisable() { super.onDisable();
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(oldTicks);
        posRenderer = null;
    }
}

package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render3DEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.WorldRender3DEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "ClickTP",
        category = Module.Category.MISCELLANEOUS
)
public class ClickTP extends Module {

    PosRenderer posRenderer;
    BlockPos posToTp;


    @Subscribe(priority = Listener.Priority.HIGHEST)
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (!(mc.currentScreen instanceof GuiScreen)) {
            posToTp = mc.player.rayTrace(999.0D,1.0F).getBlockPos();
            if (Mouse.isButtonDown(1)) {
                mc.player.setPositionAndUpdate(posToTp.getX(),posToTp.getY()+1,posToTp.getZ());
            }
        }
    }
    );

    @Override
    public void onEnable() {
        super.onEnable();
        posRenderer = new PosRenderer(this,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }


    //Render

    public Setting<Boolean> renderer = new Setting<>("Render", false, this).setMode(Setting.Mode.SUB);
    public Setting<Double> range = new Setting<>("RenderRange", (double)10, this, 1, 20).setVisibility(v-> renderer.getValue());
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("Box", PosRenderer.boxeMode.Gradient, this).setVisibility(v->  renderer.getValue());
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderer.getValue());
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderer.getValue());
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  renderer.getValue());
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  renderer.getValue());

    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("Outline", PosRenderer.outlineModes.Gradient, this).setVisibility(v-> renderer.getValue());
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && renderer.getValue());
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && renderer.getValue());
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && renderer.getValue());
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> outlineMode.getValue(PosRenderer.outlineModes.Normal) && renderer.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> !outlineMode.getValue(PosRenderer.outlineModes.None) && renderer.getValue());


    @Subscribe
    public Listener<WorldRender3DEvent> listener2 = new Listener<>(event -> {
        if (posRenderer != null && posToTp != null) {
            posRenderer.doRender(
                    posToTp,
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
    });
}

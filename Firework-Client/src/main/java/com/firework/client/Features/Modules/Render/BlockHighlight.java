package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "BlockHighlight",category = Module.Category.VISUALS)
public class BlockHighlight extends Module {

    PosRenderer posRenderer;

    public Setting<Boolean> block = new Setting<>("Block", false, this).setMode(Setting.Mode.SUB);
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this).setVisibility(v->  block.getValue());
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  block.getValue());
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) &&  block.getValue());
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  block.getValue());
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  block.getValue());



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("Outline", PosRenderer.outlineModes.Gradient, this).setVisibility(v->  block.getValue());
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && block.getValue());
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && block.getValue());
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && block.getValue());
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && block.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v->  !outlineMode.getValue(PosRenderer.outlineModes.None) && block.getValue());

    public Setting<Boolean> entity = new Setting<>("Entity", false, this).setMode(Setting.Mode.SUB);


    @Subscribe
    public Listener<Render3dE> listener2 = new Listener<>(event -> {
        RayTraceResult result = mc.objectMouseOver;
        if (result != null && result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
           BlockPos  pos = result.getBlockPos();
           if (pos != null) {
            posRenderer.doRender(
                    pos,
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

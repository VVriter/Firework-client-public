package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleManifest(name = "AntiVoid",category = Module.Category.MOVEMENT)
public class AntiVoid extends Module {


    public Setting<String> mode  = new Setting<>("Mode", "Jump", this, Arrays.asList("Jump","PacketFly"));

    public Setting<Boolean> render = new Setting<>("Render", false, this).setMode(Setting.Mode.SUB);
    public Setting<Double> renderRange = new Setting<>("RenderRange", (double)10, this, 1, 50).setVisibility(v-> render.getValue());

    private List<BlockPos> holes = new ArrayList<BlockPos>();
    @Override
    public void onTick() {
        super.onTick();
        this.holes = this.calcHoles();
        if(mode.getValue().equals("Jump")){
        if (mc.player.posY <= 0.5) {
            mc.player.moveVertical = 10.0f;
            mc.player.jump();
        }
        else {
           mc.player.moveVertical = 0.0f;
            }
        }
    }

    public void onDisable() {
        super.onDisable();
        mc.player.moveVertical = 0.0f;
        posRenderer = null;
    }


    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> voidHoles = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.renderRange.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (pos.getY() != 0 || this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
            voidHoles.add(pos);
        }
        return voidHoles;
    }



    /******************************************RENDER SHIT*************************************************/


    PosRenderer posRenderer;

    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("RenderMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values()).setVisibility(v-> render.getValue());
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("BoxMode", PosRenderer.boxeMode.Normal, this, PosRenderer.boxeMode.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue());
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue());
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) && renderMode.getValue(PosRenderer.renderModes.Box)&& render.getValue());
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box)&& render.getValue());
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) && renderMode.getValue(PosRenderer.renderModes.Box)&& render.getValue());



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("OutlineMode", PosRenderer.outlineModes.Normal, this, PosRenderer.outlineModes.values()).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && render.getValue());
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue());
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue());
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue());
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Box) && !outlineMode.getValue(PosRenderer.outlineModes.None) && render.getValue());
    public Setting<HSLColor> beaconFillColor = new Setting<>("BeaconFillColor", new HSLColor(200, 54, 43), this).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Beacon) && render.getValue());
    public Setting<Integer> beaconOutlineWidth = new Setting<>("BeaconOutlineWidth", 3, this, 1, 10).setVisibility(v-> renderMode.getValue(PosRenderer.renderModes.Beacon) && render.getValue());

    @Override
    public void onEnable(){
        super.onEnable();
        posRenderer = new PosRenderer(this,renderMode,boxMode,outlineMode);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
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
                        outlineHeightNormal.getValue().floatValue(),
                        beaconFillColor.getValue().toRGB(),
                        beaconOutlineWidth.getValue()
                );
            }
        }
    }

}

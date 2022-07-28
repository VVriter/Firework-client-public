package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;

@ModuleManifest(name = "BurrowEsp",category = Module.Category.VISUALS)
public class BurrowEsp extends Module {

    @Override
    public void onEnable(){
        super.onEnable();
        burrowPosRenderer = new PosRenderer(this,boxMode,outlineMode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        burrowPosRenderer = null;
    }

    
    public Setting<Boolean> burrowSubBool = new Setting<>("General", false, this).setMode(Setting.Mode.SUB);

    public Setting<Double> range = new Setting<>("Range", (double)40, this, 1, 100).setVisibility(v-> burrowSubBool.getValue());
    public Setting<Boolean> self = new Setting<>("Self Too", true, this).setVisibility(v-> burrowSubBool.getValue());

    ArrayList<BlockPos> burrowsList = new ArrayList<>();
    PosRenderer burrowPosRenderer;

    public Setting<Boolean> render = new Setting<>("Render", false, this).setMode(Setting.Mode.SUB);
    public Setting<PosRenderer.boxeMode> boxMode = new Setting<>("Box", PosRenderer.boxeMode.Normal, this).setVisibility(v->  render.getValue());
    public Setting<HSLColor> fillColor = new Setting<>("FillColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  render.getValue());
    public Setting<Double> boxHeightNormal = new Setting<>("BoxHeight", (double)1, this, -0.3, 5).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Normal) &&  render.getValue());
    public Setting<HSLColor> fillColor1 = new Setting<>("StartColor", new HSLColor(100, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  render.getValue());
    public Setting<HSLColor> fillColor2 = new Setting<>("EndColor", new HSLColor(200, 54, 43), this).setVisibility(v-> boxMode.getValue(PosRenderer.boxeMode.Gradient) &&  render.getValue());



    public Setting<PosRenderer.outlineModes> outlineMode = new Setting<>("Outline", PosRenderer.outlineModes.Gradient, this).setVisibility(v->  render.getValue());
    public Setting<HSLColor> gradientOutlineColor1 = new Setting<>("FirstColor", new HSLColor(1, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue());
    public Setting<HSLColor> gradientOutlineColor2 = new Setting<>("SecondColor", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Gradient) && render.getValue());
    public Setting<HSLColor> colorOutline = new Setting<>("ColorOutline", new HSLColor(200, 54, 43), this).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue());
    public Setting<Double> outlineHeightNormal = new Setting<>("OutlineHeight", (double)1, this, -0.3, 5).setVisibility(v->  outlineMode.getValue(PosRenderer.outlineModes.Normal) && render.getValue());
    public Setting<Integer> outlineWidth = new Setting<>("OutlineWidth", 3, this, 1, 10).setVisibility(v->  !outlineMode.getValue(PosRenderer.outlineModes.None) && render.getValue());

    @Subscribe
    public Listener<Render3dE> renderer = new Listener<>(e-> {
        burrowsList.clear();
                for (EntityPlayer player : mc.world.playerEntities) {
                    final BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
                    if ((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.getValue() && (blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) > 1.5 || this.self.getValue())) {
                        this.burrowsList.add(blockPos);
                    }
                }

                if (burrowsList != null) {
                    for (BlockPos pos : burrowsList) {
                        burrowPosRenderer.doRender(
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
        }
    );


}

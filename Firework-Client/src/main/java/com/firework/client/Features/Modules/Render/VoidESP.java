package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "VoidESP",category = Module.Category.RENDER)
public class VoidESP extends Module {


    public Setting<Double> range = new Setting<>("Range", (double)10, this, 1, 20);
    public Setting<Boolean> down = new Setting<>("tS", false, this);


    private List<BlockPos> holes = new ArrayList<BlockPos>();

   @Override
   public void onTick(){
       super.onTick();
       this.holes = this.calcHoles();
   }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            RenderUtils.renderCrosses(this.down.getValue() != false ? pos.up() : pos, new Color(255, 255, 255), 2.0f);
        }
    }



    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> voidHoles = new ArrayList<BlockPos>();
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), false);
        int size = positions.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = positions.get(i);
            if (pos.getY() != 0 || this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
            voidHoles.add(pos);
        }
        return voidHoles;
    }
}

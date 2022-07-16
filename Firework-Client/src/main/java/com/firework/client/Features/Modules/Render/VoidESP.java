package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
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

    static boolean down;


    public Setting<Double> range = new Setting<>("Range", (double)10, this, 1, 20);


    public Setting<Enum> mode = new Setting<>("RenderMode", modes.Crosses, this, modes.values());
    public enum modes{
        Crosses, Box
    }

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);
    public Setting<Double> wight = new Setting<>("LineWight", (double)2, this, 1, 10);
    public Setting<updowns> updown = new Setting<>("Mode", updowns.Down, this, updowns.values()).setVisibility(v-> mode.getValue(modes.Crosses));
    public enum updowns{
        Up, Down
    }
    public Setting<Double> hihgt = new Setting<>("Height", (double)2, this, 0, 10).setVisibility(v-> mode.getValue(modes.Box));


    private List<BlockPos> holes = new ArrayList<BlockPos>();

   @Override
   public void onTick(){
       super.onTick();
       this.holes = this.calcHoles();

       if(updown.getValue(updowns.Up)){
           down = true;
       }else{
           down = false;
       }
   }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        int size = this.holes.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = this.holes.get(i);
            if(mode.getValue(modes.Crosses)){
            RenderUtils.renderCrosses(this.down != false ? pos.up() : pos, new Color(color.getValue().toRGB().getRed(), color.getValue().toRGB().getGreen(), color.getValue().toRGB().getBlue()), wight.getValue().floatValue());
            }else{
                RenderUtils.drawBoxESP(pos, new Color(color.getValue().toRGB().getRed(), color.getValue().toRGB().getGreen(), color.getValue().toRGB().getBlue(), 255), wight.getValue().floatValue(), true, true, 70, hihgt.getValue().floatValue());}
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

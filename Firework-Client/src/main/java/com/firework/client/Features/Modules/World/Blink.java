package com.firework.client.Features.Modules.World;


import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.util.math.BlockPos;

@ModuleManifest(name = "Blink",category = Module.Category.WORLD)
public class Blink extends Module {



    public Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this);
    public Setting<Double> distance = new Setting<>("Distance", (double)6, this, 1, 20).setVisibility(autoDisable,true);

    public Setting<HSLColor> renderColor = new Setting<>("RenderColor", new HSLColor(1, 54, 43), this);


    BlockPos pos1, pos2;

    @Override
    public void onEnable(){
        super.onEnable();
        pos1 = mc.player.getPosition();
    }


    @Override
    public void onTick(){
        super.onTick();
        pos2 = mc.player.getPosition();
        if(autoDisable.getValue()){
            if(BlockUtil.getDistance(pos1,pos2)>distance.getValue()){
                onDisable();
            }
        }
    }


    @Override
    public void onDisable(){
        super.onDisable();

    }


}

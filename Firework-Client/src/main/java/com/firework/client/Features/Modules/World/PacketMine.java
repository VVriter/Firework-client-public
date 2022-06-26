package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "PacketMine",category = Module.Category.WORLD)
public class PacketMine extends Module {
    public Setting<Double> range = new Setting<>("Range", (double)5, this, 1, 10);

    public Setting<Boolean> queue = new Setting<>("Queue", false, this);
    public Setting<Double> queueLimit = new Setting<>("QueueLimit", (double)7, this, 1, 20).setVisibility(queue,true);

    public Setting<HSLColor> renderColor = new Setting<>("RenderColor", new HSLColor(1, 54, 43), this);

    boolean isTargeted;
    BlockPos pos;

    @Override
    public void onTick(){
        super.onTick();

        int size = this.blocks.size();
        for (int i = 0; i < size; ++i) {
            pos = this.blocks.get(i);}

        //For queue
        if(!queue.getValue()){
            blocks.clear();
        } if(blocks.size() >= queueLimit.getValue().intValue()){
            blocks.remove(1);
        } if(mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)){
            blocks.remove(pos);
        } if(BlockUtil.getDistanceFromBlockToEntity(mc.player.getPosition(),pos)>range.getValue()){
            blocks.remove(pos);
        }
    }



    private List<BlockPos> blocks = new ArrayList<BlockPos>();
    @SubscribeEvent
    public void onBlockHit(PlayerInteractEvent.LeftClickBlock e){
        pos = e.getPos();
            isTargeted = true;
            if(!blocks.contains(pos)) {
                blocks.add(pos);
            }
    }


    @SubscribeEvent
    public void doRender(RenderWorldLastEvent e){
        if(isTargeted && !queue.getValue()){
            RenderUtils.drawBoxESP(pos,new Color(renderColor.getValue().toRGB().getRed(),
                            renderColor.getValue().toRGB().getGreen(),
                            renderColor.getValue().toRGB().getBlue()),
                    1,
                    true,true,
                    200,1);
        }else{
            int size = this.blocks.size();
            for (int i = 0; i < size; ++i) {
                BlockPos pos = this.blocks.get(i);
                RenderUtils.drawBoxESP(pos,new Color(renderColor.getValue().toRGB().getRed(),
                                renderColor.getValue().toRGB().getGreen(),
                                renderColor.getValue().toRGB().getBlue()),
                        1,
                        true,true,
                        200,1);
            }
        }
    }


    @Override
    public void onDisable(){
        super.onDisable();
        blocks.clear();
    }
}

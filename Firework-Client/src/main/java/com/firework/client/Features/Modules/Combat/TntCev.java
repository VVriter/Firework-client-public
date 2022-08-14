package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "TntCev",
        category = Module.Category.COMBAT
)
public class TntCev extends Module {

    EntityPlayer target;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e->{
        target = PlayerUtil.getClosestTarget();
        if (target == null) return;
        for (BlockPos pos : trapBlocks(target)) {
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND,true,true,false);
        }
    });

    private BlockPos[] trapBlocks(EntityPlayer target) {
        BlockPos p = EntityUtil.getFlooredPos(target);
            return new BlockPos[]{
                    //First surround layer -1
                    p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1),
                    //Second surround layer (Feet)
                    p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1),
                    //Third surround layer (Face)
                    p.add(1, 1, 0), p.add(-1, 1, 0), p.add(0, 1, 1), p.add(0, 1, -1),
                    //Nearest helping block to trap upside
                    p.add(1, 2, 0), p.add(-1, 2, 0), p.add(0, 2, 1), p.add(0, 2, -1),

            };
        }

    private BlockPos farthestTrapBlock(BlockPos... blocks){
        double lastDistance = -1;
        BlockPos lastBlock = null;
        for(BlockPos blockPos : blocks){
            if(blockPos == null) continue;
            if(lastDistance == -1){
                lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                lastBlock = blockPos;
            }else{
                if(lastDistance<BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player))) {
                    lastBlock = blockPos;
                    lastDistance = BlockUtil.getDistance(blockPos, EntityUtil.getFlooredPos(mc.player));
                }
            }
        }
        return lastBlock;
    }
}

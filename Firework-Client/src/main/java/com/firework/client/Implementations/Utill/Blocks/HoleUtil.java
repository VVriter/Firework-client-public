package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class HoleUtil {
    static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.holeOffsets);

    public static List<BlockPos> calculateSingleHoles(int radius){
        //Holes list
        ArrayList<BlockPos> holes = new ArrayList<>();
        //For each block in a given radius
        for(BlockPos pos : BlockUtil.getAll(radius)){
            if(isAir(pos)){
                boolean isSafe = true;

                //Makes isSafe return false if surround offsets not contains obby or bedrock
                for(BlockPos offset : surroundOffset){
                    Block block = BlockUtil.getBlock(pos.add(offset));
                    if(block == Blocks.OBSIDIAN || block == Blocks.BEDROCK)continue;
                    isSafe = false;
                    break;
                }

                if(isSafe)
                    holes.add(pos);
            }
        }
        return holes;
    }

    public static boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}

package com.firework.client.Implementations.Utill.Blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class HoleUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.holeOffsets);

    public static List<BlockPos> calculateHoles(int radius, boolean Single, boolean Double){
        //Holes list
        ArrayList<BlockPos> holes = new ArrayList<>();
        //For each block in a given radius
        for(BlockPos pos : BlockUtil.getAll(radius)){
                if(isSingleHole(pos) && Single)
                    holes.add(pos);
                else if(isDoubleHole(pos) && Double) {
                    holes.add(pos);
                    holes.add(pos.offset(getEmptySafeSides(pos).get(0)));
                }
        }
        return holes;
    }

    public static boolean isSingleHole(BlockPos pos){
        if(!(BlockUtil.isAir(pos) && BlockUtil.isAir(pos.offset(EnumFacing.UP)))) return false;
        return getSafeSides(pos).size() == 4;
    }

    public static boolean isDoubleHole(BlockPos pos){
        if(!(BlockUtil.isAir(pos) && BlockUtil.isAir(pos.offset(EnumFacing.UP)))) return false;

        List<EnumFacing> emptyFacings = getEmptySafeSides(pos);
        if(emptyFacings.size() != 1) return false;
        if(getEmptySafeSides(pos.offset(emptyFacings.get(0))).size() != 1) return false;

        return true;
    }

    public static List<EnumFacing> getSafeSides(BlockPos pos){
        List<EnumFacing> facings = new ArrayList<>();
        for(EnumFacing face : EnumFacing.values()){
            if(face == EnumFacing.UP || face == EnumFacing.DOWN) continue;
            Block block = BlockUtil.getBlock(pos.offset(face));
            if(block == Blocks.OBSIDIAN || block == Blocks.BEDROCK)
                facings.add(face);
        }
        return facings;
    }

    public static List<EnumFacing> getUnSafeSides(BlockPos pos){
        List<EnumFacing> facings = new ArrayList<>();
        for(EnumFacing face : EnumFacing.values()){
            if(face == EnumFacing.UP || face == EnumFacing.DOWN) continue;
            Block block = BlockUtil.getBlock(pos.offset(face));
            if(block != Blocks.OBSIDIAN || block != Blocks.BEDROCK)
                facings.add(face);
        }
        return facings;
    }

    public static List<EnumFacing> getEmptySafeSides(BlockPos pos){
        List<EnumFacing> facings = new ArrayList<>();
        for(EnumFacing face : EnumFacing.values()){
            if(face == EnumFacing.UP || face == EnumFacing.DOWN) continue;
            Block block = BlockUtil.getBlock(pos.offset(face));
            if(block == Blocks.AIR)
                facings.add(face);
        }
        return facings;
    }
}

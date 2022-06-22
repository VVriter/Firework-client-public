package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Util;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

@ModuleManifest(name = "Anchor", category = Module.Category.MOVEMENT)
public class Anchor extends Module {

    public Setting<Boolean> pull = new Setting<>("Pull", true, this);
    public Setting<Double> pitch = new Setting<>("Pitch", 60d, this, 0, 90);

    private final ArrayList<BlockPos> holes = new ArrayList<BlockPos>();
    int holeblocks;
    public static boolean AnchorING;

    public boolean isBlockHole(BlockPos blockpos) {
        holeblocks = 0;
        if (Util.mc.world.getBlockState(blockpos.add(0, 3, 0)).getBlock() == Blocks.AIR) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, 2, 0)).getBlock() == Blocks.AIR) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, 1, 0)).getBlock() == Blocks.AIR) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, 0, 0)).getBlock() == Blocks.AIR) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN || Util.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Util.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Util.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN || Util.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) ++holeblocks;

        if (Util.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN || Util.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) ++holeblocks;

        if (holeblocks >= 9) return true;
        else return false;
    }
    private Vec3d Center = Vec3d.ZERO;

    public Vec3d GetCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (Util.mc.world == null) {
            return;
        }
        if (Util.mc.player.posY < 0) {
            return;
        }
        if (Util.mc.player.rotationPitch >= pitch.getValue()) {
            if (isBlockHole(getPlayerPos().down(1)) || isBlockHole(getPlayerPos().down(2)) ||
                    isBlockHole(getPlayerPos().down(3)) || isBlockHole(getPlayerPos().down(4))) {
                AnchorING = true;

                if (!pull.getValue()) {
                    Util.mc.player.motionX = 0.0;
                    Util.mc.player.motionZ = 0.0;
                } else {
                    Center = GetCenter(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ);
                    double XDiff = Math.abs(Center.x - Util.mc.player.posX);
                    double ZDiff = Math.abs(Center.z - Util.mc.player.posZ);

                    if (XDiff <= 0.1 && ZDiff <= 0.1) {
                        Center = Vec3d.ZERO;
                    }
                    else {
                        double MotionX = Center.x- Util.mc.player.posX;
                        double MotionZ = Center.z- Util.mc.player.posZ;

                        Util.mc.player.motionX = MotionX/2;
                        Util.mc.player.motionZ = MotionZ/2;
                    }
                }
            } else AnchorING = false;
        }
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Util.mc.player.posX), Math.floor(Util.mc.player.posY), Math.floor(Util.mc.player.posZ));
    }
}

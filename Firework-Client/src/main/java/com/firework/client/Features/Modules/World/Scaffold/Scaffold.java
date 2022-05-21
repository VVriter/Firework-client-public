package com.firework.client.Features.Modules.World.Scaffold;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.BlockUtil;
import net.minecraft.util.math.BlockPos;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.List;

public class Scaffold extends Module {

    public Setting<Boolean> rotate  = new Setting<>("Rotates", true, this);
    public Setting<Boolean> packet  = new Setting<>("Packet Rotate", true, this);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();
    private BlockPos pos;

    public Scaffold(){super("Scaffold",Category.WORLD);}


    @Override
    public void onTick() {
        super.onTick();
        pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (isAir(pos)) {
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), mc.player.isSneaking());
            blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
        }
    }

    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blocksToRender.clear();
    }
}

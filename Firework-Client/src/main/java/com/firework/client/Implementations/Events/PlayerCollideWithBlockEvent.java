package com.firework.client.Implementations.Events;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerCollideWithBlockEvent extends Event {

   public BlockPos pos;
   public Block block;


    public PlayerCollideWithBlockEvent(BlockPos pos, Block block) {
        this.pos = pos;
        this.block = block;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}

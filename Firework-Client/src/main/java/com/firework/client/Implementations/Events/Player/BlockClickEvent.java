package com.firework.client.Implementations.Events.Player;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Event;

public class BlockClickEvent extends Event {
    private final BlockPos pos;
    private final EnumFacing facing;

    public BlockClickEvent(BlockPos pos, EnumFacing facing){
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos(){
        return this.pos;
    }

    public EnumFacing getFacing(){
        return this.facing;
    }
}

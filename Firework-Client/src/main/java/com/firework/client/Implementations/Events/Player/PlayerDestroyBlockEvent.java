package com.firework.client.Implementations.Events.Player;

import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Event;

public class PlayerDestroyBlockEvent extends Event {
    private final BlockPos pos;

    public PlayerDestroyBlockEvent(final BlockPos pos){
        this.pos = pos;
    }

    public BlockPos getBlockPos(){
        return pos;
    }
}

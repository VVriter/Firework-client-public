package com.firework.client.Features.Modules.Movement.BlockFly;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ScaffoldBlock extends BlockPos {

    private long startTime;

    public ScaffoldBlock(Vec3d vec) {
        super(vec);
        startTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }
}
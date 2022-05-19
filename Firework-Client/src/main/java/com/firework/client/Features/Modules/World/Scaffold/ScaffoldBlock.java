package com.firework.client.Features.Modules.World.Scaffold;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

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
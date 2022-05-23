package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class PlayerUtil {
    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }
}

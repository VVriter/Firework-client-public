package com.firework.client.Features.Modules.World.HighwayTools;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "HighwayTools",category = Module.Category.WORLD)
public class HighwayTools extends Module {

    private List<BlockPos> poses = new ArrayList<BlockPos>();

    BlockPos playerPos;



    @Override
    public void onTick() {
        playerPos = mc.player.getPosition();
    }



}

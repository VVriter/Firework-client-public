package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Avoid",
        category = Module.Category.WORLD,
        description = ""
)
public class Avoid extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.world.isAreaLoaded(new BlockPos(0,120,0),3)) {
            System.out.println("OKAY");
        } else {
            System.out.println("NOT OKAY");
        }
    }

    BlockPos playerPosition;
    BlockPos avoidedPos;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> list = new Listener<>(e-> {
        playerPosition = EntityUtil.getPlayerPos(mc.player);

      //  if (playerPosition.is)
    });
}

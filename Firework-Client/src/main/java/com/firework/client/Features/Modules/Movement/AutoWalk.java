package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoWalk extends Module {
    public AutoWalk(){super("AutoWalk",Category.MOVEMENT);}
    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        event.getMovementInput().moveForward = 1.0f;
    }
}

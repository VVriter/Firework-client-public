package com.firework.client.Implementations.Events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage
{
    public UpdateWalkingPlayerEvent(final int stage) {
        super(stage);
    }
}
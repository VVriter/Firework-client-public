package com.firework.client.Implementations.Events;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Event;

public class UpdateEvent extends Event {
    private static UpdateEvent INSTANCE = new UpdateEvent();
    private TickEvent.Phase phase;

    public static UpdateEvent get(TickEvent.Phase phase) {
        UpdateEvent.INSTANCE.phase = phase;
        return INSTANCE;
    }

    public TickEvent.Phase getPhase() {
        return this.phase;
    }
}

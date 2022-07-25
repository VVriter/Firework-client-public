package com.firework.client.Implementations.Events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import ua.firework.beet.Event;

public class InputUpdateEvent extends Event {
    private final MovementInput movementInput;

    public InputUpdateEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }

    public MovementInput getMovementInput() {
        return movementInput;
    }
}

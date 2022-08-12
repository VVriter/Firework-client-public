package com.firework.client.Implementations.Events.Player;

import net.minecraft.entity.MoverType;
import ua.firework.beet.Event;

public class PlayerMoveEvent extends Event {

    public MoverType type;
    public double x;
    public double y;
    public double z;

    public PlayerMoveEvent(MoverType type, double x, double y, double z){
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

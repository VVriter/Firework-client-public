package com.firework.client.Implementations.Events;

import net.minecraft.network.Packet;
import ua.firework.beet.Event;

public class PacketEvent extends Event {

    private final Packet packet;

    public PacketEvent(final Packet packet) {
        this.packet = packet;
    }

    public final Packet getPacket() {
        return this.packet;
    }

    public final static class Receive extends PacketEvent {
        public Receive(final Packet packet) {
            super(packet);
        }
    }

    public final static class Send extends PacketEvent {
        public Send(final Packet packet) {
            super(packet);
        }
    }
}
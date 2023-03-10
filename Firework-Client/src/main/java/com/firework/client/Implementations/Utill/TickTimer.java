package com.firework.client.Implementations.Utill;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickTimer {
    private int ticks = 0;

    public TickTimer(){
        MinecraftForge.EVENT_BUS.register(this);
        ticks = 0;
    }

    public boolean hasPassedTicks(int ticks) {
        if (this.ticks >= ticks) {
            return true;
        }
        return false;
    }

    public void reset(){
        ticks = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent tick){
        this.ticks++;
    }

    public void destory(){
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}

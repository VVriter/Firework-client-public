package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import net.minecraftforge.common.MinecraftForge;

public class Manager {

    private boolean registered;

    public Manager(boolean register){
        this.registered = register;
        if(this.registered) {
            MinecraftForge.EVENT_BUS.register(this);
            Firework.eventBus.register(this);
        }
    }

    public void destory(){
        if(this.registered) {
            MinecraftForge.EVENT_BUS.unregister(this);
            Firework.eventBus.unregister(this);
        }
    }
}

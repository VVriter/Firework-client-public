package com.firework.client.Implementations.Managers;

import com.firework.client.Firework;
import net.minecraftforge.common.MinecraftForge;

public class Manager {

    private boolean registered;

    public Manager(boolean register){
        this.registered = register;
    }

    public void destory(){
        if(this.registered)
            Firework.eventBus.register(this);
    }

    public void load(){
        if(this.registered)
            Firework.eventBus.register(this);
    }
}

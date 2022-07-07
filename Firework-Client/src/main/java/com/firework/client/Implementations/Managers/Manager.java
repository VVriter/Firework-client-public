package com.firework.client.Implementations.Managers;

import net.minecraftforge.common.MinecraftForge;

public class Manager {

    private boolean registered;

    public Manager(boolean register){
        this.registered = register;
        if(this.registered)
            MinecraftForge.EVENT_BUS.register(this);
    }

    public void destory(){
        if(this.registered)
            MinecraftForge.EVENT_BUS.unregister(this);
    }
}

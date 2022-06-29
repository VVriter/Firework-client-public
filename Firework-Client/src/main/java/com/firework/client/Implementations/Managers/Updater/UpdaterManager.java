package com.firework.client.Implementations.Managers.Updater;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class UpdaterManager {

    public ArrayList<Updater> updaters;
    public int index = 0;

    public UpdaterManager(){
        this.updaters = new ArrayList<>();
    }

    public boolean containsIndex(int index){
        for(Updater updater : updaters)
            if(updater.index == index)
                return true;

        return false;
    }

    public void registerUpdater(Updater updater){
        updaters.add(updater);
    }

    public void removeUpdater(Updater updater){
        updaters.remove(updater);
    }
    public void removeUpdater(int index){
        Updater up = null;
        for(Updater updater : updaters){
            if(updater.index == index){
                up = updater;
            }
        }
        updaters.remove(up);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        for(Updater updater : updaters)
            updater.run();
    }
}

package com.firework.client.Implementations.Managers.Updater;

import com.firework.client.Firework;

public class Updater {
    private int tmpDelay;
    public int delay = 0;

    public int index;

    public Updater(){
        Firework.updaterManager.index++;
        this.index = Firework.updaterManager.index;
    }

    public void run(){
        if(tmpDelay != delay){
            tmpDelay++;
            return;
        }else{
            tmpDelay = 0;
        }
    }
}

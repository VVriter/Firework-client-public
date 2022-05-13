package com.firework.client.Managers.Module;

import com.firework.client.Modules.Module;
import com.firework.client.Modules.Client.Test;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> modules;

    public ModuleManager(){
        modules = new ArrayList<>();
        registerModules();
    }

    public void registerModules(){
        modules.add(new Test());
    }


}

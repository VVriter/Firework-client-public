package com.firework.client.Impl.Managers.Module;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.Client.Test;

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

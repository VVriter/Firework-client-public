package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.Client.Test;

import java.util.ArrayList;

public class ModuleManager {
    public static ArrayList<Module> modules;

    public ModuleManager(){
        modules = new ArrayList<>();
        RegisterModules();
    }

    public void RegisterModules(){
        modules.add(new Test());
    }


}

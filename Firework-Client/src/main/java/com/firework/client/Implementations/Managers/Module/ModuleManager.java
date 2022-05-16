package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;

import java.util.ArrayList;

public class ModuleManager {

    public ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        registerModules();
    }

    public void registerModules() {
        modules.add(new Test());
    }
}

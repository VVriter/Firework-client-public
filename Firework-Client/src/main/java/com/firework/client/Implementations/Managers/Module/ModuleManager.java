package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.Modules.Client.Gui;
import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.Movement.Sprint;
import com.firework.client.Features.Modules.Render.ItemPhysics;
import com.firework.client.Features.Modules.Render.ParticlesESP;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleManager {

    public ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        registerModules();
    }

    public void registerModules() {
        register(new Test(),
                    new Sprint(),
                    new ParticlesESP(),
                    new Gui(),
                    new ItemPhysics());
    }

    public void register(Module... module) {
        Collections.addAll(modules, module);
    }
}

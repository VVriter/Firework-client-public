package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.Modules.Client.ClickGui;
import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Combat.Bot;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.Movement.AirJump;
import com.firework.client.Features.Modules.Movement.Sprint;
import com.firework.client.Features.Modules.Combat.Velocity;
import com.firework.client.Features.Modules.Render.BetterFPS;
import com.firework.client.Features.Modules.Render.ESP;
import com.firework.client.Features.Modules.Render.ItemPhysics;
import com.firework.client.Features.Modules.Render.ParticlesESP;
import com.firework.client.Features.Modules.World.EntityControl;

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
                    new ClickGui(),
                    new AirJump(),
                    new Bot(),
                    new EntityControl(),
                    new Velocity(),
                    new BetterFPS(),
                    new ESP(),
                    new ItemPhysics());
    }

    public void register(Module... module) {
        Collections.addAll(modules, module);
    }
}

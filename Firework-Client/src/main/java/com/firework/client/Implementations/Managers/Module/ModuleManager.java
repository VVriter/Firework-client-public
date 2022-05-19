package com.firework.client.Implementations.Managers.Module;

import com.firework.client.Features.Modules.Client.ClickGui;
import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Combat.Bot;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.Movement.AirJump;
import com.firework.client.Features.Modules.Movement.MovementHelper;
import com.firework.client.Features.Modules.Combat.Velocity;
import com.firework.client.Features.Modules.Render.*;
import com.firework.client.Features.Modules.World.BridgeBuild;
import com.firework.client.Features.Modules.World.EntityControl;
import com.firework.client.Features.Modules.World.Scaffold.Scaffold;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
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
                    new AirJump(),
                    new Scaffold(),
                    new ParticlesESP(),
                    new ClickGui(),
                    new MovementHelper(),
                    new Bot(),
                    new EntityControl(),
                    new Velocity(),
                    new BetterFPS(),
                    new ESP(),
                    new CustomTime(),
                    new BridgeBuild(),
                    new NoRender(),
                    new ItemPhysics());
    }

    public void register(Module... module) {
        Collections.addAll(modules, module);
    }

    public void saveModules() throws IllegalAccessException {
        for (Module module : modules) {
            JSONObject moduleJson = new JSONObject();
            moduleJson.put("isEnabled", module.isEnabled);
            for (Field var : module.getClass().getDeclaredFields()) {
                if(Setting.class.isAssignableFrom(var.getType())) {
                    Setting setting = (Setting) var.get(this);
                    moduleJson.put("settings/"+setting.name, setting);
                }
            }
            try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "/Modules/" + module.getClass().getSimpleName() + ".json")) {
                file.write(moduleJson.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

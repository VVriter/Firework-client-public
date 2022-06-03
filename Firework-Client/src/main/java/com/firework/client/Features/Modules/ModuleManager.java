package com.firework.client.Features.Modules;

import com.firework.client.Features.Modules.Chat.*;
import com.firework.client.Features.Modules.Client.*;
import com.firework.client.Features.Modules.World.*;
import com.firework.client.Features.Modules.Movement.*;
import com.firework.client.Features.Modules.Combat.*;
import com.firework.client.Features.Modules.Render.*;
import com.firework.client.Features.Modules.Misc.*;


import com.firework.client.Features.Modules.World.Burrow.SelfBlock;
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
                    new AntiVoid(),
                    new AntiHunger(),
                    new AutoLog(),
                    new Notifications(),
                    new FastSleep(),
                    new AntiLog4j(),
                    new GreenText(),
                    new Narrator(),
                    new AutoToxic(),
                    new NoRotate(),
                    new Leave(),
                    new Bypass(),
                    new BebraGui(),
                    new Anchor(),
                    new ItemViewModel(),
                    new Fovmod(),
                    new Announcer(),
                    new AntiSpam(),
                    new ChorusControl(),
                    new AutoNametag(),
                    new SelfBlock(),
                    new SlowAnimations(),
                    new AutoWalk(),
                    new AirJump(),
                    new NoForge(),
                    new GuiGradient(),
                    new ChestSwap(),
                    new Scaffold(),
                    new AutoRespawn(),
                    new DiscordNotificator(),
                    new ParticlesESP(),
                    new MovementHelper(),
                    new Bot(),
                    new EntityControl(),
                    new Velocity(),
                    new MiddleClick(),
                    new BetterFPS(),
                    new ESP(),
                    new CustomEnchants(),
                    new CustomTime(),
                    new BridgeBuild(),
                    new NoRender(),
                    new ItemPhysics(),
                    new BoatFlyRewrote(),
                    new Spammer(),
                    new AutoFish(),
                    new F3Injection(),
                    new DiscordRPCModule());
    }

    public void register(Module... module) {
        Collections.addAll(modules, module);
    }

    public void saveModules() {
        for (Module module : modules) {
            JSONObject moduleJson = new JSONObject();
            moduleJson.put("isEnabled", module.isEnabled);
            for (Field var : module.getClass().getDeclaredFields()) {
                if(Setting.class.isAssignableFrom(var.getType())) {
                    Setting setting = null;
                    try {
                        setting = (Setting) var.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
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

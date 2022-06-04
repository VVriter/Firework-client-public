package com.firework.client.Features.Modules;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import java.util.Comparator;
import java.util.Set;

public class ModuleManager {

    public ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        registerModules();
    }

    public void registerModules() {
        Set<Class> classList = ClassFinder.findClasses(Module.class.getPackage().getName(), Module.class);
        classList.forEach(aClass -> {
            try {
                Module module = (Module) aClass.getConstructor().newInstance();
                modules.add(module);
                // lookup.put(module.getName().toLowerCase(), module);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        System.out.println("Modules initialised");
        modules.sort(Comparator.comparing(Module::getName));
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

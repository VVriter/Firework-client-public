package com.firework.client.Features.Modules;

import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import com.firework.client.Implementations.Utill.Client.Pair;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class ModuleManager {

    public ArrayList<Module> modules;
    public ArrayList<Info> infos;

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

        infos = initializeSubModules();
        infos.sort(Comparator.comparing(Info::getName));
    }

    public ArrayList<Info> initializeSubModules() {
        ArrayList<Info> infos = new ArrayList<>();

        ArrayList<Pair> pairs = new ArrayList<>();
        for (Module module : modules) {
            if (getClass().isAnnotationPresent(ModuleArgs.class)) {
                ModuleArgs args = getClass().getAnnotation(ModuleArgs.class);
                Pair<Module, String> pair = new Pair<>(module, args.subCategory());
                pairs.add(pair);
            } else {
                infos.add(module);
            }
        }

        ArrayList<String> usedSubCategories = new ArrayList<>();
        ArrayList<Pair<String, ArrayList<Module>>> subCategories = new ArrayList<>();

        for (Pair<Module, String> pair : pairs) {
            if (!usedSubCategories.contains(pair.two)) {
                usedSubCategories.add(pair.two);
                Pair<String, ArrayList<Module>> pair2 = new Pair<>(pair.two, new ArrayList<Module>());
                subCategories.add(pair2);
            } else {
                for (Pair<String, ArrayList<Module>> pair3 : subCategories) {
                    if (pair3.one == pair.two) {
                        pair3.two.add(pair.one);
                    }
                }
            }
        }

        for (Pair<String, ArrayList<Module>> pair : subCategories) {
            SubModule subModule = new SubModule(pair.one, pair.two);
            infos.add(subModule);
        }
        return infos;
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

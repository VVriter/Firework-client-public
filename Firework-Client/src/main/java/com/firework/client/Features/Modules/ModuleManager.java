package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import com.firework.client.Implementations.Utill.Client.Pair;

import java.util.ArrayList;


import java.util.Comparator;
import java.util.Set;

public class ModuleManager extends Manager {

    public ArrayList<Module> modules;
    public ArrayList<Info> infos;

    public ModuleManager() {
        super(false);
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
            } catch (Exception e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        System.out.println("Modules initialised");

        modules.sort(Comparator.comparing(Module::getName));
        infos = initializeSubModules();
    }

    public Module getModuleByName(String name){
        for(Module module : modules)
            if(module.name == name)
                return module;

        return null;
    }

    public ArrayList<Module> enabledModules(){
        ArrayList<Module> enabledModules = new ArrayList<>();
        for(Module module : modules)
            if(module.isEnabled.getValue())
                enabledModules.add(module);

        return enabledModules;
    }

    public ArrayList<Info> initializeSubModules() {
        ArrayList<Info> infos = new ArrayList<>();

        ArrayList<Pair> pairs = new ArrayList<>();
        for (Module module : modules) {
            if (getClass().isAnnotationPresent(ModuleManifest.class)) {
                ModuleManifest args = getClass().getAnnotation(ModuleManifest.class);
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
}

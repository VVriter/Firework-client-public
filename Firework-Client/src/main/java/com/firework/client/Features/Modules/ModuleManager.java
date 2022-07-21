package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Managers.Manager;
import com.firework.client.Implementations.Utill.Client.ClassFinder;
import com.firework.client.Implementations.Utill.Client.Pair;

import java.util.ArrayList;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleManager extends Manager {

    public ArrayList<Module> modules;

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
            } catch (Exception e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        System.out.println("Modules initialised");

        modules.sort(Comparator.comparing(Module::getName));
    }

    public Module getModuleByName(String name){
        for(Module module : modules)
            if(module.name == name)
                return module;

        return null;
    }

    public Module getModuleByClass(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz) return module;
        }
        return null;
    }

    public List<Module> getModules(Module.Category category) { return modules.stream().filter(module -> module.category == category).collect(Collectors.toList()); }

    public List<Module> enabledModules() { return modules.stream().filter(module -> module.isEnabled.getValue()).collect(Collectors.toList()); }
}

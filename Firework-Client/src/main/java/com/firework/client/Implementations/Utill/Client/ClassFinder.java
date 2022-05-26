package com.firework.client.Implementations.Utill.Client;

import org.reflections.Reflections;

import java.util.Set;

public class ClassFinder {
    public static Set<Class> findClasses(String pack, Class subType) {
        Reflections reflections = new Reflections(pack);
        return reflections.getSubTypesOf(subType);
    }
}

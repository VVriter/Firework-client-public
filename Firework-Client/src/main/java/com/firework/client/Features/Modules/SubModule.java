package com.firework.client.Features.Modules;

import java.util.ArrayList;

public class SubModule extends Info{

    public ArrayList<Module> modules;

    public SubModule(String name, ArrayList<Module> modules) {
        super(name);
        this.modules = modules;
    }
}

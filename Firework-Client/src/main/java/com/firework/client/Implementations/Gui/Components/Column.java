package com.firework.client.Implementations.Gui.Components;

import com.firework.client.Features.Modules.Module;

import java.util.ArrayList;

public class Column {

    public ArrayList<Module> components;

    public String name;

    int offset = 0;

    public Column(String name){
        components = new ArrayList<>();

        this.name = name;
    }

}

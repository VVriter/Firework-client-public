package com.firework.client.Implementations.UI.GuiNEO.Components;

import com.firework.client.Features.Modules.Info;

import java.util.ArrayList;

public class Column {

    public ArrayList<Info> components;

    public String name;

    int offset = 0;

    public Column(String name){
        components = new ArrayList<>();

        this.name = name;
    }

}

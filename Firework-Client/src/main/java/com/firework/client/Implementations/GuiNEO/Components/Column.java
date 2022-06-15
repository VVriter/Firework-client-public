package com.firework.client.Implementations.GuiNEO.Components;

import java.util.ArrayList;

public class Column {

    public ArrayList<Object> components;

    public String name;

    int offset = 0;

    public Column(String name){
        components = new ArrayList<>();

        this.name = name;
    }

}

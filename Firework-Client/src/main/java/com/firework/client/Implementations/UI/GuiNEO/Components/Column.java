package com.firework.client.Implementations.UI.GuiNEO.Components;

import com.firework.client.Features.Modules.Module;

import java.util.ArrayList;

public class Column {

    public ArrayList<Module> components;
    public ArrayList<Button> buttons;

    public String name;

    public int x;
    public int y;
    public int yOffset = 0;

    public boolean opened = true;
    public boolean picked = false;

    public int xPickOffset = 0;
    public int yPickOffset = 0;
    public Column(String name, int x, int y){
        components = new ArrayList<>();
        buttons = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.name = name;
    }

}

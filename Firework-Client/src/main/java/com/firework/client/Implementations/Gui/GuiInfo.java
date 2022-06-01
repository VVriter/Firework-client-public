package com.firework.client.Implementations.Gui;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Components.Column;
import com.firework.client.Implementations.Utill.Client.Pair;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.firework.client.Firework.*;

public class GuiInfo {

    //Icons list
    public static ArrayList<Pair> icons = new ArrayList<>();

    //Columns list
    public static ArrayList<Column> columns = new ArrayList<>();

    //Colors for buttons
    public static Color outlineColorA = new Color(102, 102, 102);
    public static Color outlineColorB = new Color(65, 65, 65);
    public static Color fillColorA = new Color(72, 72, 72);
    public static Color fillColorB = new Color(107, 103, 107);

    //Colors for frames
    public static Color outlineFrameColorA = new Color(76, 76, 76);
    public static Color outlineFrameColorB = new Color(115, 115, 115);

    //Gui scale
    public static double guiScale = 0.01;

    //last index
    public static int index = 0;

    //Makes columns, for each category
    public static void setupModulesColumns(){
        columns.clear();
        for(Module.Category c : Module.Category.values()){
            columns.add(new Column(c.toString()));
        }
    }

    //Adds a module to a column to what it belongs
    public static void addModuleToColumn(Module module){
        for(Column c : columns){
            if(c.name == module.category.toString())
                c.components.add(module);
        }
    }

    //Setup icons
    public static void icons(){
        icons.add(new Pair(Module.Category.COMBAT, resourceLocation("firework/textures/combat.png")));
    }

    //Has icon
    public static boolean hasCategoryIcon(String category){
        for(Pair pair : icons){
            if(((Module.Category) pair.one).toString() == category)
                return true;
        }
        return false;
    }

    //Icon from category
    public static ResourceLocation resourceLocationByCategory(String category){
        for(Pair pair : icons){
            if(((Module.Category) pair.one).toString() == category)
                return (ResourceLocation) pair.two;
        }
        return null;
    }

}

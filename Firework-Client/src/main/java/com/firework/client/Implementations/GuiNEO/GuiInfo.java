package com.firework.client.Implementations.GuiNEO;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.GuiNEO.Components.Button;
import com.firework.client.Implementations.GuiNEO.Components.Column;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Firework.resourceLocation;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.fillColorA;
import static com.firework.client.Implementations.GuiClassic.GuiInfo.outlineColorA;

public class GuiInfo {

    //Icons list
    public static ArrayList<Pair> icons = new ArrayList<>();

    //Columns list
    public static ArrayList<Column> columns = new ArrayList<>();

    //Colors for buttons
    public static Color outlineColorA = new Color(63, 63, 63);
    public static Color outlineColorB = new Color(65, 65, 65);
    public static Color outlineColorC = new Color(100, 100, 100);
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
        icons.add(new Pair(Module.Category.CLIENT, resourceLocation("firework/textures/client.png")));
        icons.add(new Pair(Module.Category.COMBAT, resourceLocation("firework/textures/combat.png")));
        icons.add(new Pair(Module.Category.MISC, resourceLocation("firework/textures/misc.png")));
        icons.add(new Pair(Module.Category.CHAT, resourceLocation("firework/textures/chat.png")));
        icons.add(new Pair(Module.Category.RENDER, resourceLocation("firework/textures/render.png")));
        icons.add(new Pair(Module.Category.MOVEMENT, resourceLocation("firework/textures/movement.png")));
        icons.add(new Pair(Module.Category.WORLD, resourceLocation("firework/textures/world.png")));
        icons.add(new Pair(Module.Category.HUD, resourceLocation("firework/textures/hud.png")));
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

    //2D render stuff
    public static void drawBaseButton(Button button, Color color1, Color color2){
        float x = button.x;
        float y = button.y;
        float width = button.width;
        float height = button.height;
        int outlineWidth = 1;

        RenderUtils2D.drawRectangle(new Rectangle(x, y, width, height), color1);

        RenderUtils2D.drawRectangleOutline(new Rectangle(x, y, width,
                height), outlineWidth, color2);
    }
}

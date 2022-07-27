package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Features.Modules.Client.Gui;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.GuiN.Components.EndBlock;
import com.firework.client.Implementations.UI.GuiN.Components.ModuleButton;

import java.util.ArrayList;

public class Frame {
    public ArrayList<Component> components = new ArrayList<>();
    public Module.Category category;

    public boolean opened;
    public double x;
    public double y;
    public int width;
    public int height;
    public boolean isDragging;
    public int xOffset;
    public int yOffset;

    public int barWidth;
    public int barHeight;

    public Frame(Module.Category category){
        this.category = category;

        this.opened = true;

        this.width = GuiInfo.barWidth;
        this.isDragging = false;
        this.xOffset = 0;
        this.yOffset = 0;
        this.barWidth = GuiInfo.barWidth;
        this.barHeight = 14;

        for(Module module : Firework.moduleManager.getModules(category)){
            components.add(new ModuleButton(module, this));
        }
        components.add(new EndBlock(x, y, width, 1));
        setupOffsets();
    }

    public void update(int mouseX, int mouseY, int state){
        components.stream().filter(component -> component instanceof ModuleButton).forEach(component -> {
            if(GuiInfo.isHoveringOnTheComponent(component, mouseX, mouseY)) {
                component.init(mouseX, mouseY, state);
            }
            ((ModuleButton) component).components.forEach(component1 -> {
                if(GuiInfo.isHoveringOnTheComponent(component1, mouseX, mouseY)) {
                    component1.init(mouseX, mouseY, state);
                }
            });
        });
    }

    public void draw(){
        setupOffsets();
        components.forEach(Component::draw);
    }

    public void setX(final int x){
        for(Component component : components)
            component.setX(x);
    }

    public void setupOffsets(){
        double offset = this.y + GuiN.offsetY + this.barHeight;
        if(opened) {
            for (Component comp : this.components) {
                comp.setOffsets(offset);
                offset += comp.getHeight();
            }
        }
    }
}

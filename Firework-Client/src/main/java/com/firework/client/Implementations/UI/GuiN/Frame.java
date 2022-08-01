package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.UI.GuiN.Components.Button;
import com.firework.client.Implementations.UI.GuiN.Components.ModuleButton;
import com.firework.client.Implementations.UI.GuiN.Components.StartButton;
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

        components.add(new StartButton(category, x, y, width, 14));
        for(Module module : Firework.moduleManager.getModules(category)){
            components.add(new ModuleButton(module, this));
        }
        setupOffsets();
    }

    public void update(int mouseX, int mouseY, int state){
        components.stream().filter(component -> component instanceof ModuleButton).forEach(component -> {
            if(GuiInfo.isHoveringOnTheComponent(component, mouseX, mouseY)) {
                component.init(mouseX, mouseY, state);
            }
            ((ModuleButton) component).components.stream()
                    .filter(component1 -> component1 instanceof Button)
                    .forEach(component2 -> {
                if(isHoveringOnTheComponent(component2, mouseX, mouseY)) {
                    component2.init(mouseX, mouseY, state);
                }
            });
        });
    }

    public void draw(int mouseX, int mouseY){
        setupOffsets();
        components.forEach(component -> component.draw(mouseX, mouseY));
    }

    public void setX(final int x){
        this.x = x;
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

    public int getExpandedHeight() {
        int height = this.height;
        for(Component component : components){
            if(component instanceof ModuleButton){
                height+=component.getHeight();
            }
        }
        return height;
    }

    public void onKeyTyped(final int keyCode){
        components.stream().filter(component -> component instanceof Button).forEach(button -> ((Button) button).onKeyTyped(keyCode));
    }


    public static boolean isHoveringOnTheComponent(Component component, int mouseX, int mouseY) {
        return mouseX > component.x && mouseX < component.x + component.width && mouseY > component.y && mouseY < component.y + component.getHeight();
    }
}

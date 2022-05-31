package com.firework.client.Implementations.Gui.Components;

import com.firework.client.Implementations.Gui.GuiInfo;

public class Button {

    public int x;
    public int y;

    public int width;
    public int height;

    public int offset = 11;

    public int index;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        GuiInfo.index++;
        this.index = GuiInfo.index;
    }

    public void draw(){}

    public void onKeyTyped(int keyCode) {}

    public void initialize(int mouseX, int mouseY){}

    public void initialize(int mouseX, int mouseY, int state){}

}

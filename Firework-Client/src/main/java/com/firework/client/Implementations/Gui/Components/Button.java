package com.firework.client.Implementations.Gui.Components;

public class Button {

    public int x;
    public int y;

    public int width;
    public int height;

    public int standartHeight = 11;
    public int offset = 11;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(){}

    public void onKeyTyped(int keyCode) {}

    public void initialize(int mouseX, int mouseY){}

    public void initialize(int mouseX, int mouseY, int state){}

}

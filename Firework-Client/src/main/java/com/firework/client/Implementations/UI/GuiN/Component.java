package com.firework.client.Implementations.UI.GuiN;

public class Component {
    public double x;
    public double y;
    public int width;
    public int height;

    public Component(double x, double y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Component(){}

    public void draw(int mouseX, int mouseY){}

    public void init(int mouseX, int mouseY, int state){}

    public void setOffsets(final double minY){
        this.y = minY;
    }

    public void setX(final int x){
        this.x = x;
    }

    public int getHeight() {
        return this.height;
    }
}

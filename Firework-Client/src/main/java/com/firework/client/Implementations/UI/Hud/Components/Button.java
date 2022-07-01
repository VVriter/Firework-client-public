package com.firework.client.Implementations.UI.Hud.Components;

import net.minecraft.util.math.Vec2f;

public class Button {
    public int x;
    public int y;
    public int width;
    public int height;

    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(){}

    public boolean initialize(Vec2f point, int state) {
        return false;
    }
}

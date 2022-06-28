package com.firework.client.Implementations.Utill.Render;

import java.awt.*;
import java.util.Random;

public class ColorUtils {

    public static Color plus(Color one, Color two){
        return new Color(one.getRed() + two.getRed(), one.getGreen() + two.getGreen(), one.getBlue() + two.getBlue());
    }

    public static Color minus(Color one, Color two){
        return new Color(one.getRed() - two.getRed(), one.getGreen() - two.getGreen(), one.getBlue() - two.getBlue());
    }

    public static Color randomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

}

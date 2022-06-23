package com.firework.client.Implementations.GuiClassic.Particles;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;

import static com.firework.client.Implementations.Utill.Client.MathUtil.*;
import static com.firework.client.Implementations.Utill.Util.mc;

public class Particle {
    public int radius;
    public Color color;
    public Vector2f dir;

    public Point2D.Double location;

    public Particle(){
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        radius = randomValue(5, 8);
        color = new HSLColor(randomValue(180, 360), 100, 50).toRGB();
        location = new Point2D.Double(randomValue(radius, scaledResolution.getScaledWidth()-radius), randomValue(radius, scaledResolution.getScaledHeight()-radius));

        dir = new Vector2f(getRandomElement(Arrays.asList(-1, -2, 1, 2)), getRandomElement(Arrays.asList(-1, -2, 1, 2)));
    }

}

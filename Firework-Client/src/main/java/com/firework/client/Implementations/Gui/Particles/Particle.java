package com.firework.client.Implementations.Gui.Particles;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static com.firework.client.Implementations.Utill.Client.MathUtil.randomValue;
import static com.firework.client.Implementations.Utill.Render.ColorUtils.randomColor;
import static com.firework.client.Implementations.Utill.Util.mc;

public class Particle {
    public int radius;
    public Color color;
    public Vector2f dir;

    public Point location;

    public Particle(){
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        radius = randomValue(5, 8);
        color = new HSLColor(randomValue(0, 270), 100, 50).toRGB();
        location = new Point(randomValue(0, scaledResolution.getScaledWidth()), randomValue(0, scaledResolution.getScaledHeight()));

        dir = new Vector2f(randomValue(-1, 1), randomValue(-1, 1));
    }

}

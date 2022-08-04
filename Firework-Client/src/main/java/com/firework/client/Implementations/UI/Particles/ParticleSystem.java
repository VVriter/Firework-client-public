package com.firework.client.Implementations.UI.Particles;

import com.firework.client.Features.Modules.Client.GuiParticles;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.Util.mc;
import static java.lang.Math.round;

public class ParticleSystem {

    //Speed timer
    public static Timer timer;

    //Speed value in ms
    public static int speed = 40;

    //Particles scale factor
    public static int scale = 1;

    //Line long
    public static double lineLong = 10;

    //Particles amount
    public static int amount = 50;

    //Particles list
    public static ArrayList<Particle> particles = new ArrayList<>();

    //Particles pairs list
    public static ArrayList<Pair> lines = new ArrayList<>();

    public ParticleSystem(){
        particles.clear();
        timer = new Timer(); timer.reset();

        for(int i = 0; i < amount; i++){
            particles.add(new Particle());
        }
    }

    //Draws Particles
    public void drawParticles(){
        for(Particle particle : particles){
            Color color = particle.color;
            if(ParticleInfo.mode == ParticleInfo.colorMode.Astolfo){
                color = new Color(RainbowUtil.astolfoColors((int) (particle.location.y * 2), 100));
            }
            RenderUtils2D.drawFilledCircle(particle.location, color, particle.radius * scale);
        }
    }

    //Draw lines
    public void drawLines() {

        for(Pair pair : lines){
            Particle one = ((Particle)pair.one);
            Particle two = ((Particle)pair.two);
            Color one1 = one.color;
            Color two1 = two.color;
            if(ParticleInfo.mode == ParticleInfo.colorMode.Astolfo){
                one1 = new Color(RainbowUtil.astolfoColors((int) (one.location.y * 2), 100));
                two1 = new Color(RainbowUtil.astolfoColors((int) (two.location.y * 2), 100));
            }
            RenderSystem.drawGradientLine(one.location, two.location, one1, two1);
        }
        lines.clear();

        for(Particle particle1 : particles){
            for(Particle particle2 : particles){
                if(RenderUtils2D.distance(particle1.location, particle2.location) <= lineLong){
                    Pair pair = new Pair(particle1, particle2);
                    if(!Pair.containsPair(lines, pair))
                        lines.add(pair);
                }
            }
        }

    }

    //Update particles positions using velocity
    public void updatePositions(){
        scale = GuiParticles.scaleFactor.getValue();
        lineLong = GuiParticles.lineLong.getValue();

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if(timer.hasPassedMs(speed)){
            timer.reset();
            for(Particle particle : particles) {
                int newX = (int) round(particle.location.x += particle.dir.x);
                int newY = (int) round(particle.location.y += particle.dir.y);

                int radius = (int) round(particle.radius*scale);

                if(newX-radius > 0 && newX+radius < scaledResolution.getScaledWidth()) {
                    particle.location.x = newX;
                }else{
                    particle.dir.x = -particle.dir.x;
                }

                if(newY-radius > 0 && newY+radius < scaledResolution.getScaledHeight()) {
                    particle.location.y = newY;
                }else{
                    particle.dir.y = -particle.dir.y;
                }
            }
        }
    }
}

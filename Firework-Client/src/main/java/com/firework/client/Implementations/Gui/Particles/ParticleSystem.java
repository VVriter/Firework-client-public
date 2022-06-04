package com.firework.client.Implementations.Gui.Particles;

import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.Util.mc;

public class ParticleSystem {

    //Speed timer
    public static Timer timer;

    //Speed value in ms
    public static int speed = 15;

    //Particles scale factor
    public static int scale = 1;

    //Particles amount
    public static int amount = 20;

    //Particles list
    public static ArrayList<Particle> particles = new ArrayList<>();

    public ParticleSystem(){
        particles.clear();
        timer = new Timer(); timer.reset();

        for(int i = 0; i < amount; i++){
            particles.add(new Particle());
        }
    }

    public void drawParticles(){
        for(Particle particle : particles){
            RenderUtils2D.drawFilledCircle(particle.location, particle.color, particle.radius);
        }
    }

    public void updatePositions(){
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if(timer.hasPassed(speed)){
            timer.reset();
            for(Particle particle : particles) {
                int newX = particle.location.x += particle.dir.x;
                int newY = particle.location.y += particle.dir.y;

                if(newX-particle.radius > 0 && newX+particle.radius < scaledResolution.getScaledWidth()) {
                    particle.location.x = newX;
                }else{
                    particle.dir.x = -particle.dir.x;
                }

                if(newY-particle.radius > 0 && newY+particle.radius < scaledResolution.getScaledHeight()) {
                    particle.location.y = newY;
                }else{
                    particle.dir.y = -particle.dir.y;
                }
            }
        }
    }
}

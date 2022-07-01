package com.firework.client.Implementations.Utill.Render;

import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.Timer;

import java.util.ArrayList;

/*
    @author PunCakeCat
 */
public class AnimationUtil {
    public double width;
    private ArrayList<Pair<Double, Boolean>> tasks = new ArrayList<>();
    private float speed;

    public void setValues(double width, float speed){
        this.tasks.add(new Pair<>(width, false));
        this.speed = speed;
    }

    public void update(){
        for (Pair<Double, Boolean> task : tasks) {
            if (!task.two) {
                if(width < task.one) {
                    if(width + speed >= task.one){
                        width = task.one;
                        task.two = true;
                        break;
                    }
                    width += speed;
                }else if(width > task.one) {
                    if(width - speed <= task.one){
                        width = task.one;
                        task.two = true;
                        break;
                    }
                    width -= speed;
                }
                task.two = true;
                break;
            }
        }
    }
}

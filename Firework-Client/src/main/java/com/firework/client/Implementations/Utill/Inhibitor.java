package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Utill.Client.Pair;

import java.util.ArrayList;

/*
    @author PunCakeCat
 */
public class Inhibitor {
    public double value = 0;
    public ArrayList<Pair<Double, Boolean>> tasks = new ArrayList<>();
    private double speed;

    public void setValues(double value, double speed){
        this.tasks.add(new Pair<>(value, false));
        this.speed = speed;
    }

    public void update(){
        for (Pair<Double, Boolean> task : tasks) {
            if (!task.two) {
                if(value < task.one) {
                    if(value + speed >= task.one){
                        value = task.one;
                        task.two = true;
                        break;
                    }
                    value += speed;
                }else if(value > task.one) {
                    if(value - speed <= task.one){
                        value = task.one;
                        task.two = true;
                        break;
                    }
                    value -= speed;
                }
            }
        }
    }
}

package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Utill.Client.Triple;

import java.util.ArrayList;

public class Inhibitor {
    private double value = 0;
    public ArrayList<Triple<Double, Double, Boolean>> tasks = new ArrayList<>();

    public void setValues(double width, double speed){
        this.tasks.add(new Triple<>(width, speed, false));
    }

    public void update(){
        for (Triple<Double, Double, Boolean> task : tasks) {
            if (!task.three) {
                if(value < task.one) {
                    if(value + task.two >= task.one){
                        value = task.one;
                        task.three = true;
                        break;
                    }
                    value += task.two;
                }else if(value > task.one) {
                    if(value - task.two <= task.one){
                        value = task.one;
                        task.three = true;
                        break;
                    }
                    value -= task.two;
                }
            }
        }
    }

    public double getValue(){
        return value;
    }

    public void setValue(double value){
        this.value = value;
    }
}

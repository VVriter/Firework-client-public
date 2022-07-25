package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;

/*
    @author PunCakeCat
 */
public class Inhibitor {
    public double value = 0;
    private double one, two;
    private double speed;
    private double negativeFactor = 1;

    public void setValues(double minValue, double maxValue, double speed){
        this.one = minValue;
        this.two = maxValue;
        this.speed = speed;
    }

    public void update(){
        value += negativeFactor * speed;
       if(value <= one && negativeFactor == -1) {
           value = one;
           negativeFactor = 1;
       }else if(value >= two && negativeFactor == 1){
           value = two;
           negativeFactor = -1;
       }
    }
}

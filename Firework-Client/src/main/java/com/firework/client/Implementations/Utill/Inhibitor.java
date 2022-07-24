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
    private AtomicDouble one = new AtomicDouble(), two = new AtomicDouble();
    private double speed;
    private double negativeFactor = 1;

    public void setValues(double minValue, double maxValue, double speed){
        this.one.set(minValue);
        this.two.set(maxValue);
        this.speed = speed;
    }

    public void update(){
        if((one.get() < two.get() && value >= two.get())) { // if one < two && value  == two -> if min < max && value == max
            MathUtil.swap(one, two); //swaps values
            negativeFactor = (two.get() - value) / Math.abs(two.get() - value); //Gets negative factor, will be "1" if max is bigger than value, and "-1' if not
        }else if(one.get() > two.get() && value <= two.get()){
            MathUtil.swap(one, two); //swaps values
            negativeFactor = (one.get() - value) / Math.abs(one.get() - value); //Gets negative factor, will be "1" if max is bigger than value, and "-1' if not
        }
        value += negativeFactor * speed;
    }
}

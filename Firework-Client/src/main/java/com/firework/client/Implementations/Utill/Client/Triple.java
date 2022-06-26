package com.firework.client.Implementations.Utill.Client;

public class Triple <U, A, F> {
    public U one;
    public A two;
    public F three;

    public Triple(U one, A two, F three){
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "one=" + one +
                ", two=" + two +
                ", three=" + three +
                '}';
    }
}

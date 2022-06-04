package com.firework.client.Implementations.Utill.Client;

import java.util.ArrayList;
import java.util.Objects;

public class Pair {

    public Object one;
    public Object two;

    public Pair(Object one, Object two){
        this.one = one;
        this.two = two;
    }

    public Pair(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(one);objects.add(two);
        if(objects.contains(((Pair)o).one) && objects.contains(((Pair)o).two)) return true;

        return false;
    }

    public static boolean containsPair(ArrayList<Pair> pairs, Pair pair){
        for(Pair pair2 : pairs)
            if(pair2.equals(pair))
                return true;

        return false;
    }
}

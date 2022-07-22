package ua.firework.beet;

import java.util.function.Consumer;

/*
    @author PunCakeCat
*/
public class Listener<T> {

    private Consumer<T> callback;
    private Priority priority;

    public Listener(Consumer<T> callback){
        this.callback = callback;
        this.priority = Priority.MEDIUM;
    }

    public Consumer<T> getCallback(){
        return callback;
    }

    public Priority getPriority(){
        return this.priority;
    }

    public Listener<T> setPriority(Priority priority){
        this.priority = priority;
        return this;
    }

    public enum Priority{
        LOWEST, LOW, MEDIUM, HIGH, HIGHEST
    }
}

package ua.firework.beet;

import net.jodah.typetools.TypeResolver;

import java.util.function.Consumer;

/*
    @author PunCakeCat
*/
public class Listener<T> {

    private Consumer<T> callback;
    private Class<?> target;
    private Priority priority;

    public Listener(Consumer<T> callback){
        this.callback = callback;
        this.target = TypeResolver.resolveRawArgument(Consumer.class, callback.getClass());
        this.priority = Priority.MEDIUM;
    }

    public Consumer<T> getCallback(){
        return callback;
    }

    public Class<?> getTarget(){
        return target;
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

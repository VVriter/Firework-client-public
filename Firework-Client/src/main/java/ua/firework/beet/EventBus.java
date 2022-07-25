package ua.firework.beet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

/*
    @author PunCakeCat
*/
public class EventBus {
    private ArrayList<Listener> listeners;
    private int size = 0;
    public EventBus(){
        this.listeners = new ArrayList<>();
    }

    public void register(Object target){
        Field[] fields = target.getClass().getFields();
        int size = fields.length;
        for (int i = 0; i < size; i++) {
            Field field = fields[i];
            if(field.isAnnotationPresent(Subscribe.class)) {
                Listener listener = null;
                try {
                    listener = (Listener) field.get(target);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listener.setPriority(field.getAnnotation(Subscribe.class).priority());
                listeners.add(listener);
            }
        }
    }

    public void unregister(Object target){
        Field[] fields = target.getClass().getFields();
        int size = fields.length;
        for (int i = 0; i < size; i++) {
            Field field = fields[i];
            if(field.isAnnotationPresent(Subscribe.class)) {
                Listener listener = null;
                try {
                    listener = (Listener) field.get(target);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listeners.remove(listener);
            }
        }
    }

    public void subscribe(Listener<?> listener){
        listeners.add(listener);
        listeners.sort(new ListenerPriorityComparator());
    }

    public void unsubscribe(Listener<?> listener){
        listeners.remove(listener);
        listeners.sort(new ListenerPriorityComparator());
        size -= 1;
    }

    public void post(Event event){
        size = listeners.size();
        for(int i = 0; i < size; i++){
            Listener<Event> listener = listeners.get(i);
            if(event.getClass().isAssignableFrom(listener.getTarget()))
                listener.getCallback().accept(event);
        }
    }

    public class ListenerPriorityComparator implements Comparator<Listener> {
        @Override
        public int compare(Listener o1, Listener o2) {
            return o1.getPriority().ordinal()-o2.getPriority().ordinal();
        }
    }
}

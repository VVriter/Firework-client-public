package ua.firework.beet;

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

    public void register(Listener<?> listener){
        listeners.add(listener);
        listeners.sort(new ListenerPriorityComparator());
    }

    public void unregister(Listener<?> listener){
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

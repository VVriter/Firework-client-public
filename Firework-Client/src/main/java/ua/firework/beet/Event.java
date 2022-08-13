package ua.firework.beet;

/**
    @author PunCakeCat
*/
public class Event {

    private Stage stage;
    private boolean canceled;
    private boolean isCancelable;

    public Event(Stage stage){
        this.stage = stage;
        this.canceled = false;
        this.isCancelable = true;
    }

    public Event(){
        this.stage = Stage.INIT;
        this.canceled = false;
        this.isCancelable = true;
    }

    public Stage getStage(){
        return this.stage;
    }

    public boolean isCancelled(){
        return canceled;
    }

    public void setCancelled(boolean canceled){
        if(isCancelable)
            this.canceled = canceled;
    }

    public void cancel() {
        if(isCancelable)
            this.canceled = canceled;
    }

    public boolean isCancelable(){
        return isCancelable;
    }

    public void setCancelable(boolean isCancelable){
        this.isCancelable = isCancelable;
    }

    public Event post(EventBus eventBus){
        eventBus.post(this);
        return this;
    }

    public enum Stage{
        PRE,
        INIT,
        POST
    }
}

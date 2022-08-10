package com.firework.client.Implementations.Managers.Rotation;

public class RotationResponse {
    private boolean responded = false;

    public void setResponded(boolean value){
        this.responded = value;
    }

    public boolean getResponse(){
        return this.responded;
    }

    public void reset(){
        this.responded = false;
    }
}

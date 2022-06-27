package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import java.util.List;

public class RenderMode {

    public renderModes mode;
    public List<Object> values;

    public RenderMode(renderModes mode, List<Object> values){
        this.mode = mode;
        this.values = values;
    }

    public static enum renderModes{
        OutLine, Gradient, Fill
    }
}

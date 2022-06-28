package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import scala.actors.threadpool.Arrays;

import java.util.List;

public class RenderMode {

    public renderModes mode;
    public List<Object> values;

    public RenderMode(renderModes mode, Object... values){
        this.mode = mode;
        this.values = Arrays.asList(values);
    }

    public static enum renderModes{
        OutLine, Gradient, Fill
    }
}

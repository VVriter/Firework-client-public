package com.firework.client.Implementations.Utill.Render.BlockRenderBuilder;

import java.util.Arrays;
import java.util.List;

public class RenderMode {

    public renderModes mode;
    public List<Object> values;

    public RenderMode(renderModes mode, Object... values){
        this.mode = mode;
        this.values = Arrays.asList(values);
    }

    public enum renderModes{
        OutLine, Fill, OutlineGradient, FilledGradient, Beacon
    }
}

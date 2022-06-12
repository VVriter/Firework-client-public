package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

public class FutureModel extends Module {



    public static Setting<Double> trX = null;
    public static Setting<Double> trY = null;
    public static Setting<Double> trZ = null;

    public static Setting<Boolean> enabled = null;



public FutureModel(){super("FutureModel",Category.RENDER);
    trX = new Setting<>("TranslateX", (double)0, this, -3, 3);
    trY = new Setting<>("TranslateY", (double)0, this, -3, 3);
    trZ = new Setting<>("TranslateZ", (double)0, this, -3, 3);
    enabled = this.isEnabled;
}

    public Setting<Double> scX = new Setting<>("ScaleX", (double)1, this, -3, 3);
    public Setting<Double> scY = new Setting<>("ScaleY", (double)1, this, -3, 3);
    public Setting<Double> scZ = new Setting<>("ScaleZ", (double)1, this, -3, 3);

    public Setting<Double> rtX = new Setting<>("RotateX", (double)0, this, -177, 177);
    public Setting<Double> rtY = new Setting<>("RotateY", (double)0, this, -177, 177);
    public Setting<Double> rtZ = new Setting<>("RotateZ", (double)0, this, -177, 177);




    @SubscribeEvent
    public void onBebra(RenderSpecificHandEvent e) {
        GL11.glScaled(scX.getValue(),scY.getValue(),scZ.getValue());


        if(rtX.getValue().floatValue()>0){
            GL11.glRotatef(rtX.getValue().floatValue(),rtX.getValue().floatValue(),0,0);}
        if(rtX.getValue().floatValue()<0){
            GL11.glRotatef(0-rtX.getValue().floatValue(),rtX.getValue().floatValue(),0,0);}


        if(rtY.getValue().floatValue()>0){
            GL11.glRotatef(rtY.getValue().floatValue(),0,rtY.getValue().floatValue(),0);}
        if(rtY.getValue().floatValue()<0){
            GL11.glRotatef(0-rtY.getValue().floatValue(),0,rtY.getValue().floatValue(),0);}


        if(rtZ.getValue().floatValue()>0){
        GL11.glRotatef(rtZ.getValue().floatValue(),0,0,rtZ.getValue().floatValue());}
        if(rtZ.getValue().floatValue()<0){
            GL11.glRotatef(0-rtZ.getValue().floatValue(),0,0,rtZ.getValue().floatValue());}
       }
    }


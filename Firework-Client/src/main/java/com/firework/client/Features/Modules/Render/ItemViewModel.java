package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Client.Test;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class ItemViewModel extends Module {

    public static Setting<Boolean> enabled = null;


    public Setting<Enum> page = new Setting<>("Page", pages.Translate, this, pages.values());
    public enum pages{
        Translate, Rotate, Scale, Charms, Misc
    }

    public static Setting<Boolean> SlowAnimations = null;
    public static Setting<Double> SlowVal = null;

    public Setting<swings> swing = new Setting<>("Swing", swings.MainHand, this, swings.values()).setVisibility(page,pages.Misc);
    public enum swings{
        MainHand,OffHand,None
    }

    public static Setting<Boolean> restTranslate = null;
    public static Setting<Double> translateX  = null;
        public static Setting<Double> translateY   = null;
        public static Setting<Double> translateZ   = null;



    public static Setting<Boolean> resetRotations = null;
        public static Setting<Double> rotateXR   = null;
        public static Setting<Double> rotateYR   = null;
        public static Setting<Double> rotateZR   = null;

        public static Setting<Double> rotateXL  = null;
        public static Setting<Double> rotateYL   = null;
        public static Setting<Double> rotateZL   = null;

    public static Setting<Boolean> resetScale = null;
        public static Setting<Double> scaleX   = null;
        public static Setting<Double> scaleY   = null;
        public static Setting<Double> scaleZ   = null;

    public ItemViewModel(){super("ItemViewModifer",Category.RENDER);

        SlowAnimations = new Setting<>("SlowAnimations", false, this).setVisibility(page,pages.Misc);
        SlowVal = new Setting<>("AnimationSpeed", (double)20, this, 1, 40).setVisibility(SlowAnimations,true);

        enabled = this.isEnabled;
        restTranslate = new Setting<>("Reset", false, this).setVisibility(page,pages.Translate);;
        translateX = new Setting<>("TranslateX", (double)0,this, -300, 300).setVisibility(page,pages.Translate);
        translateY = new Setting<>("TranslateY", (double)0,this, -300, 300).setVisibility(page,pages.Translate);
        translateZ = new Setting<>("TranslateZ", (double)0,this, -300, 300).setVisibility(page,pages.Translate);

        resetRotations = new Setting<>("Reset", false, this).setVisibility(page,pages.Rotate);
        rotateXR = new Setting<>("RotateXR", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);
        rotateYR = new Setting<>("RotateYR", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);
        rotateZR = new Setting<>("RotateZR", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);

        rotateXL = new Setting<>("RotateXL", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);
        rotateYL = new Setting<>("RotateYL", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);
        rotateZL = new Setting<>("RotateZL", (double)0,this, -300, 300).setVisibility(page,pages.Rotate);

        resetScale = new Setting<>("Reset", false, this).setVisibility(page,pages.Scale);
        scaleX = new Setting<>("ScaleX", (double)100,this, -300, 300).setVisibility(page,pages.Scale);
        scaleY = new Setting<>("ScaleY", (double)100,this, -300, 300).setVisibility(page,pages.Scale);
        scaleZ = new Setting<>("ScaleZ", (double)100,this, -300, 300).setVisibility(page,pages.Scale);
        }

        @Override
        public void onTick(){
            super.onTick();



            //Reset bools
            if (restTranslate.getValue()) {
                translateX.setValue(0D);
                translateY.setValue(0D);
                translateZ.setValue(0D);
                restTranslate.setValue(false);
            } if (resetRotations.getValue()) {
                rotateXR.setValue(0D);
                rotateYR.setValue(0D);
                rotateZR.setValue(0D);

                rotateXL.setValue(0D);
                rotateYL.setValue(0D);
                rotateZL.setValue(0D);
                resetRotations.setValue(false);
            } if (resetScale.getValue()) {
                scaleX.setValue(100D);
                scaleY.setValue(100D);
                scaleZ.setValue(100D);
                resetScale.setValue(false);
            }










            //Swing---------------------------------------------------------------------------------------------------------------------
            if(swing.getValue(swings.MainHand)){
                mc.player.swingingHand = EnumHand.MAIN_HAND;
            }else if(swing.getValue(swings.OffHand)){
                mc.player.swingingHand = EnumHand.OFF_HAND;
            }else if(swing.getValue(swings.None)){
                mc.player.isSwingInProgress = false;
                mc.player.swingProgressInt = 0;
                mc.player.swingProgress = 0.0f;
                mc.player.prevSwingProgress = 0.0f;
            }
        }
    }





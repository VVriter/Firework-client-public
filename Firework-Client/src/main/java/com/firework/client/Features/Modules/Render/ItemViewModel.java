package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;



public class ItemViewModel extends Module {

    Timer timerXR = new Timer();
    Timer timerYR = new Timer();
    Timer timerZR = new Timer();

    Timer timerXL = new Timer();
    Timer timerYL = new Timer();
    Timer timerZL = new Timer();
    public static Setting<Boolean> enabled = null;


    public Setting<Enum> page = new Setting<>("Page", pages.Translate, this, pages.values());
    public enum pages{
        Translate, Rotate, Scale, Animations, Charms, Misc
    }

    public static Setting<Boolean> SlowAnimations = null;
    public static Setting<Double> SlowVal = null;

    public Setting<swings> swing = new Setting<>("Swing", swings.MainHand, this, swings.values()).setVisibility(page,pages.Misc);
    public enum swings{
        MainHand,OffHand,None
    }

        public static Setting<Boolean> restTranslate = null;

    public static Setting<Boolean> noEat = null;
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





        //Animations
        public static Setting<Boolean> animationXR = null;
        public static Setting<Double> animationXRSpeed  = null;
        public static Setting<Boolean> animationYR = null;
        public static Setting<Double> animationYRSpeed  = null;
        public static Setting<Boolean> animationZR = null;
        public static Setting<Double> animationZRSpeed  = null;


        public static Setting<Boolean> animationXL = null;
        public static Setting<Double> animationXLSpeed  = null;
        public static Setting<Boolean> animationYL = null;
        public static Setting<Double> animationYLSpeed  = null;
        public static Setting<Boolean> animationZL = null;
        public static Setting<Double> animationZLSpeed  = null;

    public ItemViewModel(){super("ItemViewModifer",Category.RENDER);

        SlowAnimations = new Setting<>("SlowAnimations", false, this).setVisibility(page,pages.Misc);
        SlowVal = new Setting<>("AnimationSpeed", (double)20, this, 1, 40).setVisibility(SlowAnimations,true);

        enabled = this.isEnabled;
        restTranslate = new Setting<>("Reset", false, this).setVisibility(page,pages.Translate);
        noEat = new Setting<>("NoEat", true, this).setVisibility(page,pages.Translate);
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

        //Animations
        animationXR = new Setting<>("AnimationXR", false, this).setVisibility(page,pages.Animations);
        animationXRSpeed = new Setting<>("SpeedXR", (double)1,this, 0, 500).setVisibility(page,pages.Animations);
        animationYR = new Setting<>("AnimationYR", false, this).setVisibility(page,pages.Animations);
        animationYRSpeed = new Setting<>("SpeedYR", (double)1,this, 0, 500).setVisibility(page,pages.Animations);
        animationZR = new Setting<>("AnimationZR", false, this).setVisibility(page,pages.Animations);
        animationZRSpeed = new Setting<>("SpeedZR", (double)1,this, 0, 500).setVisibility(page,pages.Animations);

        animationXL = new Setting<>("AnimationXL", false, this).setVisibility(page,pages.Animations);
        animationXLSpeed = new Setting<>("ScaleX", (double)1,this, 0, 500).setVisibility(page,pages.Animations);
        animationYL = new Setting<>("AnimationYL", false, this).setVisibility(page,pages.Animations);
        animationYLSpeed = new Setting<>("SpeedYL", (double)1,this, 0, 500).setVisibility(page,pages.Animations);
        animationZL = new Setting<>("AnimationZL", false, this).setVisibility(page,pages.Animations);
        animationZLSpeed = new Setting<>("SpeedYZ", (double)1,this, 0, 500).setVisibility(page,pages.Animations);
        }

        @Override
        public void onEnable() {
            super.onEnable();
            timerXR.reset();
            timerYR.reset();
            timerZR.reset();
            timerXL.reset();
            timerYL.reset();
            timerZL.reset();
        }

        double defTranslateX;
        double defTranslateY;
        double defTranslateZ;
        @Override
        public void onTick(){
            super.onTick();

            //Animations------------------------------------------------------------------------------------------------------
            if (timerXR.hasPassedMs(20/animationXRSpeed.getValue()) && rotateXR.getValue() < 350D && animationXR.getValue()) {
                rotateXR.setValue(rotateXR.getValue()+1);
                timerXR.reset();
            } else if (rotateXR.getValue() == 350D) {
                rotateXR.setValue(-350D);
            } if (timerYR.hasPassedMs(20/animationYRSpeed.getValue( )) && rotateYR.getValue() < 350D && animationYR.getValue()) {
                rotateYR.setValue(rotateYR.getValue()+1);
                timerYR.reset();
            } else if (rotateYR.getValue() == 350D) {
                rotateYR.setValue(-350D);
            } if (timerZR.hasPassedMs(20/animationZRSpeed.getValue()) && rotateZR.getValue() < 350D && animationZR.getValue()) {
                rotateZR.setValue(rotateZR.getValue()+1);
                timerZR.reset();
            } else if (rotateZR.getValue() == 350D) {
                rotateZR.setValue(-350D);
            } if (timerXL.hasPassedMs(20/animationXLSpeed.getValue()) && rotateXL.getValue() < 350D && animationXL.getValue()) {
                rotateXL.setValue(rotateXL.getValue()+1);
                timerXL.reset();
            } else if (rotateXL.getValue() == 350D) {
                rotateXL.setValue(-350D);
            } if (timerYL.hasPassedMs(20/animationYLSpeed.getValue()) && rotateYL.getValue() < 350D && animationYL.getValue()) {
                rotateYL.setValue(rotateYL.getValue()+1);
                timerYL.reset();
            } else if (rotateYL.getValue() == 350D) {
                rotateYL.setValue(-350D);
            } if (timerZL.hasPassedMs(20/animationZLSpeed.getValue()) && rotateZL.getValue() < 350D && animationZL.getValue()) {
                rotateZL.setValue(rotateZL.getValue()+1);
                timerZL.reset();
            } else if (rotateZL.getValue() == 350D) {
                rotateZL.setValue(-350D);
            }


            //Reset bools-------------------------------------------------------------------------------------------------------
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
        @Override
        public void onDisable() {
            super.onDisable();
            timerXR.reset();
            timerYR.reset();
            timerZR.reset();
            timerXL.reset();
            timerYL.reset();
            timerZL.reset();
        }
    }





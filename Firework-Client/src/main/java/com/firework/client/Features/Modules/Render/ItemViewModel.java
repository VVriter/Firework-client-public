package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.UpdateEquippedItemEvent;
import com.firework.client.Implementations.Mixins.MixinsList.IItemRenderer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Objects;


@ModuleManifest(
        name = "ItemViewModify",
        category = Module.Category.VISUALS,
        description = "Changes arm rendering"
)
public class ItemViewModel extends Module {

    Timer timerXR = new Timer();
    Timer timerYR = new Timer();
    Timer timerZR = new Timer();

    Timer timerXL = new Timer();
    Timer timerYL = new Timer();
    Timer timerZL = new Timer();
    public static Setting<Boolean> enabled = null;


    public Setting<Enum> page = new Setting<>("Page", pages.Translate, this);
    public enum pages{
        Translate, Rotate, Scale, Animations, Charms, Misc
    }

    public static Setting<Boolean> SlowAnimations = null;
    public static Setting<Double> SlowVal = null;

    public Setting<swings> swing = new Setting<>("Swing", swings.MainHand, this).setVisibility(v-> page.getValue(pages.Misc));
    public enum swings{
        MainHand,OffHand,None
    }

    public static Setting<Boolean> oldAnimations = null;
    public static Setting<Boolean> noSwap = null;

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

    public ItemViewModel(){
        SlowAnimations = new Setting<>("SlowAnimations", false, this).setVisibility(v-> page.getValue(pages.Misc));
        SlowVal = new Setting<>("AnimationSpeed", (double)20, this, 1, 40).setVisibility(v-> SlowAnimations.getValue(true));

        enabled = this.isEnabled;
        restTranslate = new Setting<>("Reset Translate", false, this).setVisibility(v-> page.getValue(pages.Translate));
        noEat = new Setting<>("NoEat", true, this).setVisibility(v-> page.getValue(pages.Translate));
        translateX = new Setting<>("TranslateX", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Translate));
        translateY = new Setting<>("TranslateY", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Translate));
        translateZ = new Setting<>("TranslateZ", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Translate));

        resetRotations = new Setting<>("Reset Rotate", false, this).setVisibility(v-> page.getValue(pages.Rotate));
        rotateXR = new Setting<>("RotateXR", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));
        rotateYR = new Setting<>("RotateYR", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));
        rotateZR = new Setting<>("RotateZR", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));

        rotateXL = new Setting<>("RotateXL", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));
        rotateYL = new Setting<>("RotateYL", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));
        rotateZL = new Setting<>("RotateZL", (double)0,this, -300, 300).setVisibility(v-> page.getValue(pages.Rotate));

        resetScale = new Setting<>("Reset Scale", false, this).setVisibility(v-> page.getValue(pages.Scale));
        scaleX = new Setting<>("ScaleX", (double)100,this, -300, 300).setVisibility(v-> page.getValue(pages.Scale));
        scaleY = new Setting<>("ScaleY", (double)100,this, -300, 300).setVisibility(v-> page.getValue(pages.Scale));
        scaleZ = new Setting<>("ScaleZ", (double)100,this, -300, 300).setVisibility(v-> page.getValue(pages.Scale));

        //Animations
        animationXR = new Setting<>("AnimationXR", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationXRSpeed = new Setting<>("SpeedXR", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));
        animationYR = new Setting<>("AnimationYR", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationYRSpeed = new Setting<>("SpeedYR", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));
        animationZR = new Setting<>("AnimationZR", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationZRSpeed = new Setting<>("SpeedZR", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));

        animationXL = new Setting<>("AnimationXL", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationXLSpeed = new Setting<>("SpeedX", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));
        animationYL = new Setting<>("AnimationYL", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationYLSpeed = new Setting<>("SpeedYL", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));
        animationZL = new Setting<>("AnimationZL", false, this).setVisibility(v-> page.getValue(pages.Animations));
        animationZLSpeed = new Setting<>("SpeedYZ", (double)1,this, 0, 500).setVisibility(v-> page.getValue(pages.Animations));

        oldAnimations = new Setting<>("Old Animations", false, this).setVisibility(v-> page.getValue(pages.Misc));
        noSwap = new Setting<>("MoSwap", false, this).setVisibility(v-> page.getValue(pages.Misc));
        }

        @Subscribe
        public Listener<UpdateEquippedItemEvent> listener = new Listener<>(event->{
            event.setCancelled(true);
            ((IItemRenderer) mc.entityRenderer.itemRenderer).settPrevEquippedProgressMainHand(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand());
            ((IItemRenderer) mc.entityRenderer.itemRenderer).settPrevEquippedProgressOffHand(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand());
            EntityPlayerSP entityplayersp = mc.player;
            ItemStack itemstack = entityplayersp.getHeldItemMainhand();
            ItemStack itemstack1 = entityplayersp.getHeldItemOffhand();

            if (entityplayersp.isRowingBoat()) {
                ((IItemRenderer) mc.entityRenderer.itemRenderer).settEquippedProgressMainHand(MathHelper.clamp(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand() - 0.4F, 0.0F, 1.0F));
                ((IItemRenderer) mc.entityRenderer.itemRenderer).settEquippedProgressOffHand(MathHelper.clamp(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand() - 0.4F, 0.0F, 1.0F));
            }
            else {
                boolean requipM = noSwap.getValue() && net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(((IItemRenderer) mc.entityRenderer.itemRenderer).gettItemStackMainHand(), itemstack, entityplayersp.inventory.currentItem);
                boolean requipO = noSwap.getValue() && net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(((IItemRenderer) mc.entityRenderer.itemRenderer).gettItemStackOffHand(), itemstack1, -1);

                if (!requipM && !Objects.equals(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand(), itemstack))
                    ((IItemRenderer) mc.entityRenderer.itemRenderer).settItemStackMainHand(itemstack);
                if (!requipM && !Objects.equals(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand(), itemstack1))
                    ((IItemRenderer) mc.entityRenderer.itemRenderer).settItemStackOffHand(itemstack1);

                ((IItemRenderer) mc.entityRenderer.itemRenderer).settEquippedProgressMainHand(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand() + MathHelper.clamp((!requipM ? 1F * 1F * 1F : 0.0F) - ((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand(), -0.4F, 0.4F));
                ((IItemRenderer) mc.entityRenderer.itemRenderer).settEquippedProgressOffHand(((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand() + MathHelper.clamp((float)(!requipO ? 1 : 0) - ((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand(), -0.4F, 0.4F));
            }

            if (((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressMainHand() < 0.1F) {
                ((IItemRenderer) mc.entityRenderer.itemRenderer).settItemStackMainHand(itemstack);
            }

            if (((IItemRenderer) mc.entityRenderer.itemRenderer).gettEquippedProgressOffHand() < 0.1F) {
                ((IItemRenderer) mc.entityRenderer.itemRenderer).settItemStackOffHand(itemstack1);
            }
        });

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





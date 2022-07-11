package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@ModuleManifest(name = "TestNot",category = Module.Category.CLIENT)
public class TestNotifications extends Module {

    Timer startMoveTimer = new Timer();
    Timer endMoveTimer = new Timer();
    Timer waitTimer = new Timer();

    boolean shudMoveLeft;
    boolean isShudMoveRight;

    int xAdd = -30;
    @Override
    public void onEnable() {
        super.onEnable();
        shudMoveLeft = true;
        isShudMoveRight = false;
        startMoveTimer.reset();
        endMoveTimer.reset();
        waitTimer.reset();
    }


    @Override
    public void onDisable() {
        super.onDisable();
        shudMoveLeft = false;
        isShudMoveRight = false;
        startMoveTimer.reset();
        endMoveTimer.reset();
        waitTimer.reset();
    }


    @SubscribeEvent
    public void event(TickEvent.RenderTickEvent e) {
            doBox();
    }

     void doBox() {
         ScaledResolution sr = new ScaledResolution(mc);


         if (shudMoveLeft && xAdd != -200 && startMoveTimer.hasPassedMs(20)) {
             xAdd = xAdd -1;
             startMoveTimer.reset();
         }

         if (xAdd == -200) {
                returner();
         }

         if (isShudMoveRight && endMoveTimer.hasPassedMs(20)) {
             xAdd = xAdd + 1;
             endMoveTimer.reset();
         }
         RenderUtils2D.drawGradientRectangleOutline(new Rectangle(sr.getScaledWidth()+xAdd,sr.getScaledHeight()-80,200,50), 2, Color.RED,Color.CYAN);
     }

    void returner() {
        if (waitTimer.hasPassedMs(2000)) {
            shudMoveLeft = false;
            isShudMoveRight = true;
        }
    }

}

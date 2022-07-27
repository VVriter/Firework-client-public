package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(name = "Notifications",category = Module.Category.CLIENT)
public class Notifications extends Module {

    public static Setting<Boolean> enabled = null;
    public Notifications() {
        enabled = this.isEnabled;
    }
    static Timer startMoveTimer = new Timer();
    static Timer endMoveTimer = new Timer();
    static Timer waitTimer = new Timer();
    static boolean shudMoveLeft;
    static boolean isShudMoveRight;

    public static boolean needToRender;

    int xAdd = -1;

   public static String header;
   public static String footer;

    public static void notificate(String header1, String footer1) {
      //  SoundUtill.playSound(new ResourceLocation("firework/audio/pop2.wav"));
        needToRender = true;
        header = header1;
        footer = footer1;
        shudMoveLeft = true;
        isShudMoveRight = false;
        startMoveTimer.reset();
        endMoveTimer.reset();
        waitTimer.reset();
    }

    @Subscribe
    public Listener<Render2dE> listener = new Listener<>(e -> {
        if (needToRender) {
            doBox();
        }
    });

     void doBox() {
         ScaledResolution sr = new ScaledResolution(mc);


         if (shudMoveLeft && xAdd != -200 && startMoveTimer.hasPassedMs(2)) {
             xAdd = xAdd -1;
             startMoveTimer.reset();
         }

         if (xAdd == -200) {
                returner();
         }

         if (isShudMoveRight && endMoveTimer.hasPassedMs(2) && xAdd != -1) {
             xAdd = xAdd + 1;
             endMoveTimer.reset();
         }

         RenderUtils2D.drawGradientRectHorizontal(new Rectangle(sr.getScaledWidth()+xAdd,sr.getScaledHeight()-80,200,50),new Color(166, 29, 124,150),new Color(86, 45, 189,150));
         RenderUtils2D.drawGradientRectangleOutline(new Rectangle(sr.getScaledWidth()+xAdd,sr.getScaledHeight()-80,200,50), 2, Color.RED,Color.CYAN);
         Firework.customFontForAlts.drawCenteredString(header,sr.getScaledWidth()+xAdd+110,sr.getScaledHeight()-75,Color.white.getRGB());
         mc.getTextureManager().bindTexture(new ResourceLocation("firework/notifications/book.png"));
         RenderUtils2D.drawCompleteImage(sr.getScaledWidth()+xAdd+3,sr.getScaledHeight()-75,40,38);
     }

    void returner() {
        if (waitTimer.hasPassedMs(2000)) {
            shudMoveLeft = false;
            isShudMoveRight = true;
        }
    }

}

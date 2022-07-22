package com.firework.client.Implementations.UI.Hud.Huds.Render.PlayerModelHud;

import com.firework.client.Firework;
import com.firework.client.Implementations.Managers.Fps.FpsManager;
import com.firework.client.Implementations.Managers.PlayTime.PlayTimeManager;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.HudInfo;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.*;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.Util.mc;
import static java.lang.Math.round;

@HudManifest(name = "PlayerModel")
public class PlayerModelHud extends HudComponent {

    @Override
    public void initialize() {
        super.initialize();
        this.height = 12;
        this.width = 20;
        this.y = 100;
        this.x = 100;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();
        String toRender = String.valueOf("FPS: " + FpsManager.getCurrendFps());
        //Draws hud background
        if(!enabled && !(mc.currentScreen instanceof HudGui)) return;
        if(mc.player == null && mc.world == null) return;
        EntityRenderer.renderEntity(mc.player,20,x,y);
    }
}
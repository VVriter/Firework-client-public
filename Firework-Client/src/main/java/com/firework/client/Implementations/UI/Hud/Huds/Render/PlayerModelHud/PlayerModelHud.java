package com.firework.client.Implementations.UI.Hud.Huds.Render.PlayerModelHud;

import com.firework.client.Implementations.Managers.FpsManager;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;

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
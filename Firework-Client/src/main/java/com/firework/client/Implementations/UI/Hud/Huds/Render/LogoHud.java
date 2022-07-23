package com.firework.client.Implementations.UI.Hud.Huds.Render;

import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.util.ResourceLocation;

import com.firework.client.Implementations.Managers.PlayTime.PlayTimeManager;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import static com.firework.client.Implementations.Utill.Util.mc;

@HudManifest(name = "LogoHud")
public class LogoHud extends HudComponent {

    @Override
    public void initialize() {
        super.initialize();
        this.height = 30;
        this.width = 100;
        this.y = 30;
        this.x = 300;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();
        String toRender = PlayTimeManager.getTimeNow();
        //Draws hud background
        if(!enabled && !(mc.currentScreen instanceof HudGui)) return;
        if(mc.player == null && mc.world == null) return;

        mc.getTextureManager().bindTexture(new ResourceLocation("firework/firework.png"));
        RenderUtils2D.drawCompleteImage(x+3,y,100,38);
    }
}
package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.ForgeNameTagEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "NameTags",
        category = Module.Category.VISUALS,
        description = "Render custom nametags"
)
public class NameTags extends Module {

    @Subscribe
    public Listener<ForgeNameTagEvent> evvv = new Listener<>(e-> {
        e.setCancelled(true);
    });

    public Setting<Double> scale = new Setting<>("Scale", (double)3, this, 0, 10);
    public Setting<Double> yOffset = new Setting<>("Y Offset", (double)2.3, this, 0, 10);
    public Setting<HSLColor> textColor = new Setting<>("TextColor", new HSLColor(1, 54, 43), this);

    public Setting<Boolean> borderSubBool = new Setting<>("Border", false, this).setMode(Setting.Mode.SUB);
    public Setting<BorderMode> borderMode = new Setting<>("BorderMode", BorderMode.Rounded, this).setVisibility(v-> borderSubBool.getValue());
    public enum BorderMode{Rounded, Normal, None}



    @Subscribe
    public Listener<Render3dE> ec = new Listener<>(e-> {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.isEntityEqual(mc.player)) continue;

            Vec3d vec = player.getPositionVector();
            String text = player.getDisplayNameString()+" "+Math.round(player.getHealth());

            GlStateManager.pushMatrix();
            Minecraft mc = Minecraft.getMinecraft();
            glBillboardDistanceScaled((float) vec.x, (float) (vec.y + yOffset.getValue()), (float) vec.z, mc.player, scale.getValue().floatValue());
            GlStateManager.disableDepth();
            GlStateManager.translate(-((double) Firework.textManager.getStringWidth(text) / 2.0), 0.0, 0.0);

            switch (borderMode.getValue()) {
                case Rounded:
                    RenderRound.drawRound(0.0f, 0.0f,Firework.textManager.getStringWidth(text),10,1,true, Color.WHITE);
                    break;
                case Normal:
                    RenderUtils2D.drawRectangleOutline(new Rectangle(0.0f, 0.0f,Firework.textManager.getStringWidth(text),10),3,Color.WHITE);
                    break;
            }

            Firework.textManager.drawStringWithShadow(text, 0.0f, 0.0f, textColor.getValue().toRGB().getRGB());

            GlStateManager.popMatrix();
        }
    });

    void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (float) distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.translate((double) x - mc.getRenderManager().viewerPosX, (double) y - mc.getRenderManager().viewerPosY, (double) z - mc.getRenderManager().viewerPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }
}

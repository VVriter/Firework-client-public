package com.firework.client.Features.AltManagerV2;

import com.firework.client.Features.AltManagerV2.auth.SessionManager;
import com.firework.client.Features.AltManagerV2.gui.GuiAccountManager;
import com.firework.client.Features.AltManagerV2.gui.GuiMicrosoftAuth;
import com.firework.client.Features.AltManagerV2.utils.Notification;
import com.firework.client.Features.AltManagerV2.utils.TextFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.StringUtils;

public class Events {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onTick(final TickEvent.RenderTickEvent event) {
        if (mc.currentScreen == null) {
            return;
        }

        // Current username
        if (
                mc.currentScreen instanceof GuiWorldSelection ||
                        mc.currentScreen instanceof GuiMultiplayer ||
                        mc.currentScreen instanceof GuiAccountManager
        ) {
            GlStateManager.disableLighting();
            final String label = TextFormatting.translate("&r&7Username&r");
            final String username = TextFormatting.translate(String.format(
                    "&r&3%s&r", SessionManager.getSession().getUsername()
            ));
            final int width = Math.max(
                    mc.fontRenderer.getStringWidth(label) / 2,
                    mc.fontRenderer.getStringWidth(username)
            );
            Gui.drawRect(
                    9 - 3, 9 - 3, 9 + width + 3, 9 + 5 + mc.fontRenderer.FONT_HEIGHT + 3,
                    0x64000000
            );
            Gui.drawRect(
                    9 - 3, 9 - 3, 9 + width + 3, 9 - 3 + 1,
                    0xFF000000
            );
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1.0);
            mc.currentScreen.drawString(
                    mc.fontRenderer, label,
                    9 * 2, 9 * 2, -1
            );
            GlStateManager.popMatrix();
            mc.currentScreen.drawString(
                    mc.fontRenderer, username,
                    9, 9 + 5, -1
            );
            GlStateManager.enableLighting();
        }

        // Notification
        if (
                mc.currentScreen instanceof GuiAccountManager ||
                        mc.currentScreen instanceof GuiMicrosoftAuth
        ) {
            final String notificationText = Notification.getNotificationText();
            if (!StringUtils.isBlank(notificationText)) {
                GlStateManager.disableLighting();
                mc.currentScreen.drawCenteredString(
                        mc.fontRenderer, notificationText,
                        mc.currentScreen.width / 2, 7, Notification.getColor()
                );
                GlStateManager.enableLighting();
            }
        }
    }

    @SubscribeEvent
    public void initGuiEvent(final InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
            event.getButtonList().add(new GuiButton(
                    69, event.getGui().width - 106, 6, 100, 20, "Accounts"
            ));
        }
    }

    @SubscribeEvent
    public void onClick(final ActionPerformedEvent event) {
        if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
            if (event.getButton().id == 69) {
                mc.displayGuiScreen(new GuiAccountManager(event.getGui(), true));
            }
        }
    }
}
package com.firework.client.Features.IngameGuis;

import com.firework.client.Features.IngameGuis.GuiDisconnected.DisconnectedGui;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Loader {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent e) {
            if (e.getGui() instanceof GuiDisconnected) {
                e.setGui(new DisconnectedGui());
        }
    }
}

package com.firework.client.Features.IngameGuis;

import com.firework.client.Features.IngameGuis.GuiDisconnected.DisconnectedGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

import static com.firework.client.Features.Modules.Module.mc;

public class Loader {

    private Field book_edited;
    private Field book_signed;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent e) {
            if (e.getGui() instanceof GuiDisconnected) {
                e.setGui(new DisconnectedGui());
            }
        }
    }

package com.firework.client.Implementations.Utill.Chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil  {
    Minecraft mc = Minecraft.getMinecraft();

    private static final String prefix = ChatFormatting.RED + "[FIREWORK] " + ChatFormatting.RESET;

    public static void sendClientMessage(String string, boolean deleteOld) {
        if (Minecraft.getMinecraft().player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        if (deleteOld) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, -727);
        } else {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component);
        }
    }

    public static void sendClientMessage(String string, int id) {
        if (Minecraft.getMinecraft().player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component, id);
    }

    public static void sendError(String string, int id) {
        if (Minecraft.getMinecraft().player == null)
            return;
        final ITextComponent component = new TextComponentString(   prefix +ChatFormatting.RED+ string+ChatFormatting.RESET);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component, id);
    }

}
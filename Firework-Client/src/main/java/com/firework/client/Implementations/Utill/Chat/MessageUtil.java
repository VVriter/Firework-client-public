package com.firework.client.Implementations.Utill.Chat;

import com.firework.client.Firework;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class MessageUtil  {

    private static final String prefix = ChatFormatting.RED + "[FIREWORK] " + ChatFormatting.RESET;

    public static void sendClientMessage(String string, boolean deleteOld) {
        if (Firework.minecraft.player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        if (deleteOld) {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, -727);
        } else {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessage(component);
        }
    }

    public static void sendClientMessage(String string, int id) {
        if (Firework.minecraft.player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        Firework.minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component, id);
    }

    public static void sendError(String string, int id) {
        if (Firework.minecraft.player == null)
            return;
        final ITextComponent component = new TextComponentString(   prefix +ChatFormatting.RED+ string+ChatFormatting.RESET);
        Firework.minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component, id);
    }

    public static void sendShown(String string,String showable, boolean deleteOld) {
        if (Firework.minecraft.player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        component.setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(ChatFormatting.RED+showable))));
        if (deleteOld) {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, -727);
        } else {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessage(component);
        }
    }

    public static void sendClickable(String string, String commandValue, boolean deleteOld) {
        if (Firework.minecraft.player == null)
            return;
        final ITextComponent component = new TextComponentString(prefix +ChatFormatting.GRAY+ string+ChatFormatting.RESET);
        component.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,commandValue)));
        if (deleteOld) {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, -727);
        } else {
            Firework.minecraft.ingameGUI.getChatGUI().printChatMessage(component);
        }
    }



}
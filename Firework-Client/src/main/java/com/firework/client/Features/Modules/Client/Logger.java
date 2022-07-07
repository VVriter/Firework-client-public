package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.AltManagerV2.utils.TextFormatting;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;

@ModuleManifest(name = "Logger",category = Module.Category.CLIENT)
public class Logger extends Module {

    public static Setting<Boolean> onModuleEnable;
    public static Setting<Boolean> onModuleDisable;

    public static Setting<Boolean> enabled = null;
    public Logger(){
        onModuleEnable = new Setting<>("OnEnableLog", true, this);
        onModuleDisable = new Setting<>("OnDisableLog", true, this);
        enabled = this.isEnabled;
        this.onEnable();
    }

    public static void log(Module module){
        System.out.println("FIREWORK: " + "Module with the name "+module.name+" is" + (module.isEnabled.getValue() ? " enabled" : " disabled") + "!");
        addMessage( getModuleStatusColor(module) + module.name + (module.isEnabled.getValue() ? " enabled" : " disabled"), false);
    }

    public static void addMessage(String message, boolean silent) {
        String prefix = TextFormatting.LIGHT_PURPLE + "<FireWork> " + TextFormatting.RESET;
        if (silent) mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + message), 1); else mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + message));
    }

    public static ChatFormatting getModuleStatusColor(Module module){
        return module.isEnabled.getValue() ? ChatFormatting.GREEN : ChatFormatting.RED;
    }

    public static void logAboutLoad(){
        System.out.println("FFFFFF  IIIIII  RRRRRRRRRRR  EEEEEEEEEE  WWWW    WWW     WWW  OOOOOOOOOOOO  RRRRRRRRRRR  KK    KK");
        System.out.println("FFF       II    RRR     RRR  EEE          WWW    WWW    WWW   OOO      OOO  RRR     RRR  KK   KK");
        System.out.println("FFFFFF    II    RRRRRRRRRRR  EEEEEEEEEE    WWW   WWW   WWW    OOO      OOO  RRRRRRRRRRR  KK KK");
        System.out.println("FF        II    RRR    RRR   EEE            WWW  WWW  WWW     OOO      OOO  RRR    RRR   KK  KK");
        System.out.println("FF      IIIIII  RRR     RRR  EEEEEEEEEE      WWWWWWWWWW       OOOOOOOOOOOO  RRR     RRR  KK    KK\n");
    }
}

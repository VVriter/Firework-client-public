package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "CommandLineLogger",category = Module.Category.CLIENT)
public class CommandLineLogger extends Module {

    public static Setting<Boolean> onModuleEnable;
    public static Setting<Boolean> onModuleDisable;



    public static Setting<Boolean> enabled = null;
    public CommandLineLogger(){
        onModuleEnable = new Setting<>("OnEnableLog", false, this);
        onModuleDisable = new Setting<>("OnDisableLog", false, this);
        enabled = this.isEnabled;
        this.onEnable();
    }

    public static void log(String msg){
        System.out.println("FIREWORK: "+msg);
    }


    @Override
    public void onEnable(){
        super.onEnable();
        log("CommandLineLogger is enabled!");
    }


    public static void logAboutLoad(){
        System.out.println("FFFFFF  IIIIII  RRRRRRRRRRR  EEEEEEEEEE  WWWW    WWW     WWW  OOOOOOOOOOOO  RRRRRRRRRRR  KK    KK");
        System.out.println("FFF       II    RRR     RRR  EEE          WWW    WWW    WWW   OOO      OOO  RRR     RRR  KK   KK");
        System.out.println("FFFFFF    II    RRRRRRRRRRR  EEEEEEEEEE    WWW   WWW   WWW    OOO      OOO  RRRRRRRRRRR  KK KK");
        System.out.println("FF        II    RRR    RRR   EEE            WWW  WWW  WWW     OOO      OOO  RRR    RRR   KK  KK");
        System.out.println("FF      IIIIII  RRR     RRR  EEEEEEEEEE      WWWWWWWWWW       OOOOOOOOOOOO  RRR     RRR  KK    KK\n");
    }
}

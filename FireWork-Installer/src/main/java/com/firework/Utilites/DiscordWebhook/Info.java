package com.firework.Utilites.DiscordWebhook;


import com.firework.Utilites.HwidCheck.HwidUtill;

public class Info {
    public static void sendinfo(){
        DiscordSender.doThing("**Session Started ---------------------** ```NN with os name "+System.getProperty("os.name")+
                " loaded Firework client installerm```"+"``` Hwid is: "+ HwidUtill.getHwid()+"```",Cfg.notify);
    }
}

package com.firework.Utilites.DiscordWebhook;

public class Info {
    public static void sendinfo(){
        DiscordSender.doThing("**Session Started ---------------------** ```NN with os name "+System.getProperty("os.name")+
                " loaded Firework client installerm",Cfg.notify);
    }
}

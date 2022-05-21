package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiSpam extends Module {


    public Setting<Boolean> Links = new Setting<>("Links", true, this);
    public Setting<Boolean> Shop = new Setting<>("Shop", true, this);
    public Setting<Boolean> Kit = new Setting<>("Kit", true, this);
    public Setting<Boolean> Nig = new Setting<>("Racism", true, this);
    public Setting<Boolean> Discord = new Setting<>("Discord", true, this);


    public AntiSpam(){super("AntiSpam",Category.MISC);}
    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent e){

        if(Links.getValue() && e.getMessage().getUnformattedText().contains("http")){e.setCanceled(true);}


        if(Shop.getValue() && e.getMessage().getUnformattedText().contains("Shop")
                || e.getMessage().getUnformattedText().contains("shop")
                ||e.getMessage().getUnformattedText().contains("Sh0p")
                || e.getMessage().getUnformattedText().contains("sh0p")
                ||  e.getMessage().getUnformattedText().contains("шоп")
                ||  e.getMessage().getUnformattedText().contains("маг")
                ||  e.getMessage().getUnformattedText().contains("фед")
                ||  e.getMessage().getUnformattedText().contains("бес")){e.setCanceled(true);}


        if(Kit.getValue() && e.getMessage().getUnformattedText().contains("Kit")
                || e.getMessage().getUnformattedText().contains("kits")
                ||  e.getMessage().getUnformattedText().contains("кит")
                ||  e.getMessage().getUnformattedText().contains("Кит")
                ||  e.getMessage().getUnformattedText().contains("К1т")
                ||  e.getMessage().getUnformattedText().contains("к1т")){e.setCanceled(true);}


        if(Nig.getValue() && e.getMessage().getUnformattedText().contains("Nig")
                ||  e.getMessage().getUnformattedText().contains("nig")
                || e.getMessage().getUnformattedText().contains("n1g")
                ||  e.getMessage().getUnformattedText().contains("нег")){e.setCanceled(true);}


        if(Discord.getValue() && e.getMessage().getUnformattedText().contains("disc")
                ||  e.getMessage().getUnformattedText().contains("Disc")
                || e.getMessage().getUnformattedText().contains("дис")
                || e.getMessage().getUnformattedText().contains("Дис") ){e.setCanceled(true);}
    }
}

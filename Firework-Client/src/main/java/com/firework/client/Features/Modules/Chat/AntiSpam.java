package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "AntiSpam",category = Module.Category.CHAT)
public class AntiSpam extends Module {

    public Setting<Boolean> Badwords = new Setting<>("Badwords", true, this);
    public Setting<Boolean> Links = new Setting<>("Links", true, this);
    public Setting<Boolean> Shop = new Setting<>("Shop", true, this);
    public Setting<Boolean> Kit = new Setting<>("Kit", true, this);
    public Setting<Boolean> Nig = new Setting<>("Racism", true, this);
    public Setting<Boolean> Discord = new Setting<>("Discord", true, this);
    public Setting<Boolean> YouTube = new Setting<>("YouTube", true, this);


   @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {

        if (Links.getValue() && e.getMessage().getUnformattedText().contains("http")) {
            e.setCanceled(true);
        }


        if (Shop.getValue() && e.getMessage().getUnformattedText().contains("Shop")
                || e.getMessage().getUnformattedText().contains("shop")
                || e.getMessage().getUnformattedText().contains("Sh0p")
                || e.getMessage().getUnformattedText().contains("sh0p")
                || e.getMessage().getUnformattedText().contains("шоп")
                || e.getMessage().getUnformattedText().contains("Шоп")
                || e.getMessage().getUnformattedText().contains("маг")
                || e.getMessage().getUnformattedText().contains("фед")
                || e.getMessage().getUnformattedText().contains("бес")) {
            e.setCanceled(true);
        }


        if (Kit.getValue() && e.getMessage().getUnformattedText().contains("Kit")
                || e.getMessage().getUnformattedText().contains("kits")
                || e.getMessage().getUnformattedText().contains("кит")
                || e.getMessage().getUnformattedText().contains("Кит")
                || e.getMessage().getUnformattedText().contains("К1т")
                || e.getMessage().getUnformattedText().contains("к1т")) {
            e.setCanceled(true);
        }


        if (Nig.getValue() && e.getMessage().getUnformattedText().contains("Nig")
                || e.getMessage().getUnformattedText().contains("nig")
                || e.getMessage().getUnformattedText().contains("n1g")
                || e.getMessage().getUnformattedText().contains("Nig")
                || e.getMessage().getUnformattedText().contains("N1g")
                || e.getMessage().getUnformattedText().contains("Нег")
                || e.getMessage().getUnformattedText().contains("нег")) {
            e.setCanceled(true);
        }


        if (Discord.getValue() && e.getMessage().getUnformattedText().contains("disc")
                || e.getMessage().getUnformattedText().contains("Disc")
                || e.getMessage().getUnformattedText().contains("#")
                || e.getMessage().getUnformattedText().contains("дис")
                || e.getMessage().getUnformattedText().contains("Дис")) {
            e.setCanceled(true);
        }

        if (Badwords.getValue() && e.getMessage().getUnformattedText().contains("fag")
                //American
                || e.getMessage().getUnformattedText().contains("ped")
                || e.getMessage().getUnformattedText().contains("dum")
                || e.getMessage().getUnformattedText().contains("ass")
                || e.getMessage().getUnformattedText().contains("fat")
                || e.getMessage().getUnformattedText().contains("fuc")
                || e.getMessage().getUnformattedText().contains("dick")
                || e.getMessage().getUnformattedText().contains("Ped")
                || e.getMessage().getUnformattedText().contains("Dum")
                || e.getMessage().getUnformattedText().contains("Ass")
                || e.getMessage().getUnformattedText().contains("Fat")
                || e.getMessage().getUnformattedText().contains("Fuc")
                || e.getMessage().getUnformattedText().contains("Dick")


                //Russian
                || e.getMessage().getUnformattedText().contains("ху")
                || e.getMessage().getUnformattedText().contains("пиз")
                || e.getMessage().getUnformattedText().contains("бл")
                || e.getMessage().getUnformattedText().contains("моча")
                || e.getMessage().getUnformattedText().contains("шлю")
                || e.getMessage().getUnformattedText().contains("ху")
                || e.getMessage().getUnformattedText().contains("еб")
                || e.getMessage().getUnformattedText().contains("ёб")
                || e.getMessage().getUnformattedText().contains("впиз")
                || e.getMessage().getUnformattedText().contains("жоп")
                || e.getMessage().getUnformattedText().contains("выб")
                || e.getMessage().getUnformattedText().contains("дерьмо")
                || e.getMessage().getUnformattedText().contains("сын")
                || e.getMessage().getUnformattedText().contains("мам")
                || e.getMessage().getUnformattedText().contains("дое")
                || e.getMessage().getUnformattedText().contains("жир")
                || e.getMessage().getUnformattedText().contains("жид")
                || e.getMessage().getUnformattedText().contains("зае")
                || e.getMessage().getUnformattedText().contains("нае")
                || e.getMessage().getUnformattedText().contains("уеб")
                || e.getMessage().getUnformattedText().contains("уёб")
                || e.getMessage().getUnformattedText().contains("Ху")
                || e.getMessage().getUnformattedText().contains("Пиз")
                || e.getMessage().getUnformattedText().contains("Бл")
                || e.getMessage().getUnformattedText().contains("Моча")
                || e.getMessage().getUnformattedText().contains("Шлю")
                || e.getMessage().getUnformattedText().contains("Ху")
                || e.getMessage().getUnformattedText().contains("Еб")
                || e.getMessage().getUnformattedText().contains("Ёб")
                || e.getMessage().getUnformattedText().contains("Впиз")
                || e.getMessage().getUnformattedText().contains("Жоп")
                || e.getMessage().getUnformattedText().contains("Выб")
                || e.getMessage().getUnformattedText().contains("Дерьмо")
                || e.getMessage().getUnformattedText().contains("Сын")
                || e.getMessage().getUnformattedText().contains("Мам")
                || e.getMessage().getUnformattedText().contains("Дое")
                || e.getMessage().getUnformattedText().contains("Жир")
                || e.getMessage().getUnformattedText().contains("Жид")
                || e.getMessage().getUnformattedText().contains("Зае")
                || e.getMessage().getUnformattedText().contains("Нае")
                || e.getMessage().getUnformattedText().contains("Уеб")
                || e.getMessage().getUnformattedText().contains("Уёб")
                || e.getMessage().getUnformattedText().contains("Ана")
                || e.getMessage().getUnformattedText().contains("ана")
                || e.getMessage().getUnformattedText().contains("Очк")
                || e.getMessage().getUnformattedText().contains("очк")
                || e.getMessage().getUnformattedText().contains("Су")
                || e.getMessage().getUnformattedText().contains("су")
                || e.getMessage().getUnformattedText().contains("ху")) {
            e.setCanceled(true);
        }


        if (YouTube.getValue() && e.getMessage().getUnformattedText().contains("yt")
                || e.getMessage().getUnformattedText().contains("tub")
                || e.getMessage().getUnformattedText().contains("ash")
                || e.getMessage().getUnformattedText().contains("заходи")
                || e.getMessage().getUnformattedText().contains("join")
                || e.getMessage().getUnformattedText().contains("ash")
                || e.getMessage().getUnformattedText().contains("video")
                || e.getMessage().getUnformattedText().contains("ют")
                || e.getMessage().getUnformattedText().contains("канал")
                || e.getMessage().getUnformattedText().contains("chan")) {
            e.setCanceled(true);
        }
    }
}

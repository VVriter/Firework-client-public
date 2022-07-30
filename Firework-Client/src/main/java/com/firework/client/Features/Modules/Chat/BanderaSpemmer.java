package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Render.UpdateEquippedItemEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.firework.client.Implementations.Utill.Timer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "BaderaSpam",
        category = Module.Category.CHAT
)
public class BanderaSpemmer extends Module {

    public Setting<Double> delay = new Setting<>("delay", (double)3, this, 1, 60);

    String S1 = "Батько наш - Бандера, Україна - мати,";
    String S2 = "Ми за Україну будем воювати!";
    String S3 = "Ой, у лісі, лісі, під дубом зеленим,";
    String S4 = "Там лежить повстанець тяженько ранений. ";
    String S5 = "Ой, лежить він, лежить, терпить тяжкі муки,";
    String S6 = "Без лівої ноги, без правої руки.";
    String S7 = "Як прийшла до нього рідна мати його,";
    String S8 = "Плаче і ридає, жалує його.  ";
    String S9 = "Ой, сину ж мій, сину, вже навоювався,";
    String S10 = "Без правої ручки, без ніжки зостався.";
    String S11 = "Мами ж наші, мами, не плачте за нами,";
    String S12 = "Не плачте за нами гіркими сльозами.";
    String S13 = "Батько наш - Бандера, Україна - мати,";
    String S14 = "Ми за Україну будем воювати!";
    String S15 = "А ми з москалями та й не в згоді жили,";
    String S16 = "На самого Петра у бій ми вступили.";
    String S17 = "Москалі тікали, аж лапті губили,";
    String S18 = "А наші за ними постріли били.";
    String S19 = "Батько наш - Бандера, Україна - мати,";
    String S20 = "Ми за Україну будем воювати!";
    String S21 = "Ой, як мати сина свого поховала";
    String S22 = "На його могилі слова написала.";
    String S23 = "На його могилі слова написала:";
    String S24 = "Слава Україні! Всім героям слава!";

    List<String> list = new ArrayList<>();

    Timer time = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        list.clear();
        cur = 1;


        File file = new File(Firework.FIREWORK_DIRECTORY + "Bandera/" + "Bandera" + ".json");
        if (!file.exists()) {
            createBandera();
        }

        for (int i = 1; i<=24; i++){
            File theDir = new File(Firework.FIREWORK_DIRECTORY+"Bandera");
            if (theDir.exists()){
                try {
                    Reader reader = new FileReader(theDir+"/Bandera.json");
                    JsonParser parser = new JsonParser();
                    JsonObject config = new Gson().fromJson(parser.parse(reader), JsonObject.class);
                    list.add(config.get(String.valueOf(i)).getAsString());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                    }
                }
            }
    }

    int cur;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        if (time.hasPassedS(delay.getValue())) {
            mc.player.sendChatMessage(list.get(cur));
            if (cur == 23) {
                cur = 1;
            } else {
                cur++;
            }
            time.reset();
        }
    });



    void createBandera() {
        File theDir = new File(Firework.FIREWORK_DIRECTORY+"Bandera");
        if (!theDir.exists()){
            theDir.mkdirs();}

        JsonObject obj = new JsonObject();
        obj.addProperty("1",S1);
        obj.addProperty("2",S2);
        obj.addProperty("3",S3);
        obj.addProperty("4",S4);
        obj.addProperty("5",S5);
        obj.addProperty("6",S6);
        obj.addProperty("7",S7);
        obj.addProperty("8",S8);
        obj.addProperty("9",S9);
        obj.addProperty("10",S10);
        obj.addProperty("11",S11);
        obj.addProperty("12",S12);
        obj.addProperty("13",S13);
        obj.addProperty("14",S14);
        obj.addProperty("15",S15);
        obj.addProperty("16",S16);
        obj.addProperty("17",S17);
        obj.addProperty("18",S18);
        obj.addProperty("19",S19);
        obj.addProperty("20",S20);
        obj.addProperty("21",S21);
        obj.addProperty("22",S22);
        obj.addProperty("23",S23);
        obj.addProperty("24",S24);

        try (FileWriter file = new FileWriter(Firework.FIREWORK_DIRECTORY + "Bandera/" + "Bandera" + ".json")) {
            MessageUtil.sendClientMessage("Starting Bandera", -1117);

            new GsonBuilder().setPrettyPrinting().create().toJson(obj,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "AutoLagMessage",
        category = Module.Category.CHAT,
        description = "Sends lag messages when player pops totem"
)
public class AutoLagMessage extends Module {
    String LAGS = "āȁ́Ёԁ\u0601܁ࠁँਁଁก༁ခᄁሁጁᐁᔁᘁᜁ᠁ᤁᨁᬁᰁᴁḁἁ ℁∁⌁␁━✁⠁⤁⨁⬁Ⰱⴁ⸁⼁、\u3101㈁㌁㐁㔁㘁㜁㠁㤁㨁㬁㰁㴁㸁㼁䀁䄁䈁䌁䐁䔁䘁䜁䠁䤁䨁䬁䰁䴁丁企倁儁刁匁吁唁嘁圁堁夁威嬁封崁币弁态愁戁持搁攁昁朁栁椁樁欁氁洁渁漁瀁焁爁猁琁甁瘁省码礁稁笁簁紁縁缁老脁舁茁萁蔁蘁蜁蠁褁訁謁谁贁踁輁送鄁鈁錁鐁锁阁霁頁餁騁鬁鰁鴁鸁鼁ꀁꄁꈁꌁꐁꔁꘁ꜁ꠁ꤁ꨁꬁ각괁긁꼁뀁넁눁댁됁딁똁뜁렁뤁먁묁밁봁";

    @Subscribe
    public Listener<TotemPopEvent> listener = new Listener<>(e-> {
        if(e.getEntity().getName() == mc.player.getName()) return;
        mc.player.sendChatMessage("/msg "+e.getEntity().getName()+" "+LAGS);
    });
}

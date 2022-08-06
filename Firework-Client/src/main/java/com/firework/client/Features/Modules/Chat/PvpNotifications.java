package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import net.minecraft.entity.Entity;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "PvpNotifications",
        category = Module.Category.CHAT,
        description = "Some pvp notifications"
)
public class PvpNotifications extends Module {
    public Setting<Boolean> trap = new Setting<>("Trap", true, this);
    public Setting<Boolean> totem = new Setting<>("TotemPop", true, this);

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        /*
        * Notifies when u trapped!
         */
        if (trap.getValue()) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (PlayerUtil.IsEntityTrapped(entity)) {
                MessageUtil.sendPvpNotification(entity.getName()+" trapped", -1117);
                }
            }
        }
    });

    @Subscribe
    public Listener<TotemPopEvent> ls = new Listener<>(e-> {
        if (totem.getValue()) {
            MessageUtil.sendPvpNotification(e.getEntity().getName()+"has popped totem",-1118);
        }
    });
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import net.minecraft.network.play.client.CPacketUseEntity;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "NoFriendDamage",
        category = Module.Category.COMBAT,
        description = "Prevents your friend from hit damage"
)
public class NoFriendDamage extends Module {
    @Subscribe
    public static Listener<PacketEvent.Send> evv = new Listener<>(event-> {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && com.firework.client.Implementations.Managers.FriendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    });
}

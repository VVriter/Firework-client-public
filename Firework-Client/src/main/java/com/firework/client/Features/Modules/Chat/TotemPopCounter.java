package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.DeathEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

@ModuleManifest(
        name = "TotemPopCounter",
        category = Module.Category.CHAT,
        description = ""
)
public class TotemPopCounter extends Module {
    public static ConcurrentHashMap totemMap = new ConcurrentHashMap();
    @Override
    public void onToggle() {
        super.onToggle();
        totemMap.clear();
    }

     void update() {
        Iterator iterator = totemMap.keySet().iterator();

        while (iterator.hasNext()) {
            EntityLivingBase entity = (EntityLivingBase) iterator.next();

            if (!mc.world.loadedEntityList.contains(entity)) {
                totemMap.remove(entity);
            }
        }
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e-> {
        if (mc.player != null && mc.world != null) {
            update();
        } else {
            totemMap.clear();
        }
    });

    @Subscribe
    public Listener<PacketEvent.Receive> lis = new Listener<>(event-> {
        if (mc.player != null && mc.world != null) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                EntityLivingBase entity = (EntityLivingBase) packet.getEntity(mc.world);

                if (packet.getOpCode() == 35) {
                    if (totemMap.containsKey(entity)) {
                        int times = ((Integer) totemMap.get(entity)).intValue() + 1;

                        totemMap.remove(entity);
                        totemMap.put(entity, Integer.valueOf(times));
                    } else {
                        totemMap.put(entity, Integer.valueOf(1));
                    }
                }
            }
        }
    });

    public static int getPops(EntityLivingBase entity) {
        return totemMap.containsKey(entity) ? ((Integer) totemMap.get(entity)).intValue() : 0;
    }

    public static int getPops(String name) {
        boolean flag = false;
        EntityLivingBase e = null;
        Iterator iterator = mc.world.loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLivingBase && entity.getName().equals(name)) {
                flag = true;
                e = (EntityLivingBase) entity;
                break;
            }
        }

        return flag && totemMap.containsKey(e) ? ((Integer) totemMap.get(e)).intValue() : 0;
    }


    @Subscribe
    public Listener<TotemPopEvent> evv = new Listener<>(e-> {
        if (getPops(e.getEntity().getName()) != 0) {
            MessageUtil.sendPvpNotification(e.getEntity().getName()+" poped "+getPops(e.getEntity().getName()) + " totems!",-1117);
        } else {
            MessageUtil.sendPvpNotification(e.getEntity().getName()+" poped "+getPops(e.getEntity().getName())+1 + " totems!",-1117);
        }
    });

    @Subscribe
    public Listener<DeathEvent> evvvv = new Listener<>(e-> {
        totemMap.remove(e.getEntity());
    });
}

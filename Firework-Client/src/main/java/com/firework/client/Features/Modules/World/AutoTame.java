package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;


@ModuleManifest(
        name = "AutoTame",
        category = Module.Category.WORLD,
        description = "Automatically tames animals"
)
public class AutoTame extends Module {

    public Setting<Double> Delay = new Setting<>("Delay", (double)3, this, 0, 5);

    private AbstractHorse EntityToTame = null;
    private Timer timer = new Timer();


    @Override
    public void onEnable() {
        super.onEnable();
        MessageUtil.sendClientMessage("Right click an animal you want to tame",-1117);
        EntityToTame = null;
    }


    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(e -> {
        if (e.getPacket() instanceof CPacketUseEntity) {
            if (EntityToTame != null)
                return;

            final CPacketUseEntity l_Packet = (CPacketUseEntity) e.getPacket();

            Entity l_Entity = l_Packet.getEntityFromWorld(mc.world);

            if (l_Entity instanceof AbstractHorse) {
                if (!((AbstractHorse) l_Entity).isTame()) {
                    EntityToTame = (AbstractHorse) l_Entity;
                    MessageUtil.sendClientMessage("Will try to tame " + l_Entity.getName(),-1117);
                }
            }
        }
    });

    @Override
    public void onUpdate() {
        if (EntityToTame == null)
            return;

        if (EntityToTame.isTame())
        {
            MessageUtil.sendClientMessage("Successfully tamed " + EntityToTame.getName() + ", disabling.",-1117);
            onDisable();
            return;
        }

        if (mc.player.isRiding())
            return;

        if (mc.player.getDistance(EntityToTame) > 5.0f)
            return;

        if (!timer.hasPassedMs(Delay.getValue() * 1000))
            return;

        timer.reset();
        mc.getConnection().sendPacket(new CPacketUseEntity(EntityToTame, EnumHand.MAIN_HAND));
    }


}

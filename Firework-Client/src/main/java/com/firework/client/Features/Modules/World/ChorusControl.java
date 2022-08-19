package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@ModuleManifest(
        name = "ChorusControl",
        category = Module.Category.WORLD
)
public class ChorusControl extends Module {

    public Setting<Boolean> cpacketplayer = new Setting<>("cpacketplayer", false, this);
    public Setting<Boolean> spacketplayerposlook = new Setting<>("spacketplayerposlook", false, this);

    public Setting<Boolean> render = new Setting<>("render", false, this);

    Queue<CPacketPlayer> packets = new LinkedList<>();
    Queue<CPacketConfirmTeleport> teleportPackets = new LinkedList<>();

    SPacketPlayerPosLook sPacketPlayerPosLook;

    @Subscribe
    public Listener<PacketEvent.Receive> lis = new Listener<>(event-> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            sPacketPlayerPosLook = (SPacketPlayerPosLook) event.getPacket();
            if (spacketplayerposlook.getValue()) event.setCancelled(true);
        }

        if (event.getPacket() instanceof CPacketPlayer) {
            packets.add(((CPacketPlayer) event.getPacket()));

            if (cpacketplayer.getValue())
                event.setCancelled(true);
        }

        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            teleportPackets.add(((CPacketConfirmTeleport) event.getPacket()));
            event.setCancelled(true);
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        while (!this.packets.isEmpty()) {
            mc.getConnection().sendPacket(Objects.requireNonNull(this.packets.poll()));
        }
        while (!this.teleportPackets.isEmpty()) {
            mc.getConnection().sendPacket(Objects.requireNonNull(this.teleportPackets.poll()));
        }
        sPacketPlayerPosLook = null;
    }

    @Subscribe
    public Listener<Render3dE> ex = new Listener<>(e-> {
        if (sPacketPlayerPosLook == null) return;
        if (!render.getValue()) return;

        BlockPos pos = new BlockPos(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
        RenderUtils.drawBoxESP(pos, Color.RED,1,true,true,160,1);
    });
}

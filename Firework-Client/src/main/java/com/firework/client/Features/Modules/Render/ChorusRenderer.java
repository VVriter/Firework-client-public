package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "ChorusRenderer",
        category = Module.Category.VISUALS
)
public class ChorusRenderer extends Module {

    SPacketPlayerPosLook sPacketPlayerPosLook;

    @Subscribe
    public Listener<PacketEvent.Receive> evv = new Listener<>(e-> {
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            sPacketPlayerPosLook = (SPacketPlayerPosLook) e.getPacket();
        }
    });


    @Subscribe
    public Listener<Render3dE> ev = new Listener<>(e-> {
        if (sPacketPlayerPosLook == null) return;
        BlockPos pos = new BlockPos(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
        RenderUtils.drawBoxESP(pos, Color.RED,1,true,true,160,1);
    });
}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "Borders",
        category = Module.Category.VISUALS,
        description = "Draws map arts border"
)
public class Borders extends Module {
    @Subscribe
    public Listener<Render3dE> render3dEListener = new Listener<>(e-> {
        RenderUtils.drawLine(new Vec3d(center().x-64,center().y,center().z-64),new Vec3d(center().x+64,center().y,center().z-64),5,Color.MAGENTA);
        RenderUtils.drawLine(new Vec3d(center().x+64,center().y,center().z+64),new Vec3d(center().x-64,center().y,center().z+64),5,Color.MAGENTA);

        RenderUtils.drawLine(new Vec3d(center().x+64,center().y,center().z-64),new Vec3d(center().x+64,center().y,center().z+64),5,Color.MAGENTA);
        RenderUtils.drawLine(new Vec3d(center().x-64,center().y,center().z+64),new Vec3d(center().x-64,center().y,center().z-64),5,Color.MAGENTA);
    });

    Vec3d center() {
        int xi;
        int zi;

        int i = 128 * (1 << 0);
        int j = MathHelper.floor((mc.player.posX + 64.0D) / (double)i);
        int k = MathHelper.floor((mc.player.posZ + 64.0D) / (double)i);
        xi = j * i + i / 2 - 64;
        zi = k * i + i / 2 - 64;

        return new Vec3d(xi,mc.player.getPositionVector().y,zi);
    }

}

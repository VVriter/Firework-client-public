package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Iterator;

@ModuleManifest(
        name = "NameTags",
        category = Module.Category.VISUALS,
        description = "Renders cool nametags"
)
public class NameTags extends Module {

    double x;
    double y;
    double z;


    //******************************Smooth pos calculations*****************************//
    @Subscribe
    public Listener<Render3dE> ev = new Listener<>(e-> {
        Iterator iterator = mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            Entity entity = (Entity) o;

            if (entity instanceof EntityPlayer && entity.isEntityAlive()) {
                double x = this.interpolate(entity.lastTickPosX, entity.posX, 1) - mc.getRenderManager().viewerPosX;
                double y = this.interpolate(entity.lastTickPosY, entity.posY, 1) - mc.getRenderManager().viewerPosY;
                double z = this.interpolate(entity.lastTickPosZ, entity.posZ,1) - mc.getRenderManager().viewerPosZ;

                this.x = x;
                this.y = y;
                this.z = z;
            }
        }
    });
    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }




}

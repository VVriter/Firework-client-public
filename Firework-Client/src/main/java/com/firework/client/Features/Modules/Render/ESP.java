package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderEntityUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.RayTraceResult;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "ESP",
        category = Module.Category.VISUALS,
        description = "Highlights storages"
)
public class ESP extends Module {


    public Setting<Boolean> storagesSubBool = new Setting<>("Storages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> chest = new Setting<>("Chest", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> echest = new Setting<>("EnderChest", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> beacon = new Setting<>("Beacon", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> dispenser = new Setting<>("Dispenser", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> dropper = new Setting<>("Dropper", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> hopper = new Setting<>("Hopper", true, this).setVisibility(v-> storagesSubBool.getValue());

    public Setting<Boolean> entitiesSubBool = new Setting<>("Entities", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> minecart = new Setting<>("Minecart", true, this).setVisibility(v-> entitiesSubBool.getValue());
    public Setting<Boolean> armorStand = new Setting<>("ArmorStand", true, this).setVisibility(v-> entitiesSubBool.getValue());
    public Setting<Boolean> boat = new Setting<>("Boat", true, this).setVisibility(v-> entitiesSubBool.getValue());


    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        for (Object c : mc.world.loadedTileEntityList) {
            if (c instanceof TileEntityChest && chest.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityChest) c).getPos(), new Color(241, 206, 34,160),3,true,true,160,1);
            } if (c instanceof TileEntityEnderChest && echest.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityEnderChest) c).getPos(),new Color(172, 34, 229,160),3,true,true,160,1);
            } if (c instanceof TileEntityBeacon && beacon.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityBeacon) c).getPos(),new Color(1,111,255,160),3,true,true,160,1);
            } if (c instanceof TileEntityDispenser && dispenser.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityDispenser) c).getPos(),new Color(72, 66, 66),3,true,true,160,1);
            } if (c instanceof TileEntityDropper && dropper.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityDispenser) c).getPos(),new Color(72, 66, 66),3,true,true,160,1);
            } if (c instanceof TileEntityHopper && hopper.getValue()) {
                RenderUtils.drawBoxESP(((TileEntityHopper) c).getPos(),new Color(72, 66, 66),3,true,true,160,1);
            }
        }

        for (Object c : mc.world.loadedEntityList) {
            if (c instanceof EntityMinecartChest && minecart.getValue()) {
                RenderUtils.drawBoxESP(((EntityMinecartChest) c).getPosition(),Color.RED,3,true,true,160,1);
            }  if (c instanceof EntityArmorStand && armorStand.getValue()) {
                RenderUtils.drawBoxESP(((EntityArmorStand) c).getPosition(),Color.magenta,3,true,true,160,2);
            }  if (c instanceof EntityBoat && boat.getValue()) {
                RenderEntityUtils.drawESP((Entity) c,1,1,255,150,1);
            } 
        }
    });
}

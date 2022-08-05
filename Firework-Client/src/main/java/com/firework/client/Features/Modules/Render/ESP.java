package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Test;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderEntityUtils;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "ESP",
        category = Module.Category.VISUALS,
        description = "Highlights storages"
)
public class ESP extends Module {

    public Setting<Boolean> playerSubBool = new Setting<>("Player", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> player = new Setting<>("Enable", true, this).setVisibility(v-> playerSubBool.getValue());

    public Setting<Boolean> storagesSubBool = new Setting<>("Storages", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> chest = new Setting<>("Chest", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> echest = new Setting<>("EnderChest", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> beacon = new Setting<>("Beacon", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> dispenser = new Setting<>("Dispenser", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> dropper = new Setting<>("Dropper", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> hopper = new Setting<>("Hopper", true, this).setVisibility(v-> storagesSubBool.getValue());
    public Setting<Boolean> frame = new Setting<>("Frame", true, this).setVisibility(v-> storagesSubBool.getValue());

    public Setting<Boolean> entitiesSubBool = new Setting<>("Entities", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> minecart = new Setting<>("Minecart", true, this).setVisibility(v-> entitiesSubBool.getValue());
    public Setting<Boolean> armorStand = new Setting<>("ArmorStand", true, this).setVisibility(v-> entitiesSubBool.getValue());
    public Setting<Boolean> boat = new Setting<>("Boat", true, this).setVisibility(v-> entitiesSubBool.getValue());

    public Setting<Boolean> miscSubBool = new Setting<>("Misc", false, this).setMode(Setting.Mode.SUB);
    public Setting<Mode> mode = new Setting<>("Mode", Mode.Sphere , this).setVisibility(v-> miscSubBool.getValue());
    public enum Mode{
        Sphere, Circle
    }
    public Setting<Boolean> pearl = new Setting<>("Pearl", true, this).setVisibility(v-> miscSubBool.getValue());
    public Setting<Boolean> exp = new Setting<>("ExpBottle", true, this).setVisibility(v-> miscSubBool.getValue());
    public Setting<Boolean> items = new Setting<>("Items", true, this).setVisibility(v-> miscSubBool.getValue());
    public Setting<Boolean> enderEye = new Setting<>("EnderEye", true, this).setVisibility(v-> miscSubBool.getValue());


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
            }  if (c instanceof EntityItemFrame && frame.getValue()) {
                BlockPos pos = new BlockPos(((EntityItemFrame) c).getPosition().add(0,-1,0));
                RenderUtils.drawBoxESP(pos,new Color(107, 84, 8),3,true,true,160,1);
            }  if (c instanceof EntityEnderPearl && pearl.getValue()) {
                if (mode.getValue(Mode.Sphere)) {
                    RenderUtils.drawSphere(((EntityEnderPearl) c).getPositionVector(),0.3,3,new Color(RainbowUtil.generateRainbowFadingColor(1,true)));
                } else if (mode.getValue(Mode.Circle)) {
                    RenderUtils.drawCircle(((EntityEnderPearl) c).getPositionVector(),0.3, new Color(RainbowUtil.generateRainbowFadingColor(1,true)),3);
                }
            }  if (c instanceof EntityExpBottle && exp.getValue()) {
                if (mode.getValue(Mode.Sphere)) {
                    RenderUtils.drawSphere(((EntityExpBottle) c).getPositionVector(),0.3,3,new Color(RainbowUtil.generateRainbowFadingColor(1,true)));
                } else if (mode.getValue(Mode.Circle)) {
                    RenderUtils.drawCircle(((EntityExpBottle) c).getPositionVector(),0.3, new Color(RainbowUtil.generateRainbowFadingColor(1,true)),3);
                }
            }  if (c instanceof EntityItem && items.getValue()) {
                if (mode.getValue(Mode.Sphere)) {
                    RenderUtils.drawSphere(((EntityItem) c).getPositionVector(),0.3,3,new Color(RainbowUtil.generateRainbowFadingColor(1,true)));
                } else if (mode.getValue(Mode.Circle)) {
                    RenderUtils.drawCircle(((EntityItem) c).getPositionVector(),0.3, new Color(RainbowUtil.generateRainbowFadingColor(1,true)),3);
                }
            }   if (c instanceof EntityEnderEye && enderEye.getValue()) {
                if (mode.getValue(Mode.Sphere)) {
                    RenderUtils.drawSphere(((EntityEnderEye) c).getPositionVector(),0.3,3,new Color(RainbowUtil.generateRainbowFadingColor(1,true)));
                } else if (mode.getValue(Mode.Circle)) {
                    RenderUtils.drawCircle(((EntityEnderEye) c).getPositionVector(),0.3, new Color(RainbowUtil.generateRainbowFadingColor(1,true)),3);
                }
            }




            if (c instanceof EntityPlayer && player.getValue()) {
                RenderUtils.FillLine((Entity) c,((EntityPlayer) c).getEntityBoundingBox());
            }
        }
    });
}

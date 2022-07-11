package com.firework.client.Features.Modules.Render.ESP.Stuff;

import com.firework.client.Features.Modules.Render.ESP.ESP;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.tileentity.*;

import static com.firework.client.Features.Modules.Module.mc;

public class StorageEsp {
    public static void doEsp() {
        for (Object c : mc.world.loadedTileEntityList) {
            if (ESP.Chests.getValue()) {
                if (c instanceof TileEntityChest) {
                    RenderUtils.blockESP(((TileEntityChest) c).getPos());
                }}

            if (ESP.Hoper.getValue()) {
                if (c instanceof TileEntityHopper) {
                    RenderUtils.blockESP(((TileEntityHopper) c).getPos());
                }}

            if (ESP.Dropper.getValue()) {
                if (c instanceof TileEntityDropper) {
                    RenderUtils.blockESP(((TileEntityDropper) c).getPos());
                }}

            if (ESP.Dispanser.getValue()) {
                if (c instanceof TileEntityDispenser) {
                    RenderUtils.blockESP(((TileEntityDispenser) c).getPos());
                }}

            if (ESP.Bed.getValue()) {
                if (c instanceof TileEntityBed) {
                    RenderUtils.blockESP(((TileEntityBed) c).getPos());
                }}

            if (ESP.Spawner.getValue()) {
                if (c instanceof TileEntityMobSpawner  ) {
                    RenderUtils.blockESP(((TileEntityMobSpawner) c).getPos());
                }}

            if (ESP.Shulker.getValue()) {
                if (c instanceof TileEntityShulkerBox  ) {
                    RenderUtils.blockESP(((TileEntityShulkerBox) c).getPos());
                }}

            if (ESP.Beacon.getValue()) {
                if (c instanceof TileEntityBeacon  ) {
                    RenderUtils.blockESP(((TileEntityBeacon) c).getPos());
                }}



            if (ESP.EChests.getValue()) {
                if (c instanceof TileEntityEnderChest ) {
                    RenderUtils.blockESP(((TileEntityEnderChest) c).getPos());
                }
            }
        }
    }
}

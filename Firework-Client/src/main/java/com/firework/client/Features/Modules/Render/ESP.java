package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ESP extends Module {

    AxisAlignedBB box = null;

    public Setting<Boolean> pull9 = new Setting<>("Player", true, this);

    public static Setting<HSLColor> playerColor2 = null;
    public static Setting<HSLColor> playerColor1 = null;




    public Setting<Boolean> pull10 = new Setting<>("Items", true, this);

    public Setting<Boolean> pull = new Setting<>("Chest", true, this);
    public Setting<Boolean> pull1 = new Setting<>("EnderChest", true, this);
    public Setting<Boolean> pull7 = new Setting<>("Shulker", true, this);
    public Setting<Boolean> pull2 = new Setting<>("Hopper", true, this);
    public Setting<Boolean> pull3 = new Setting<>("Dropper", true, this);
    public Setting<Boolean> pull4 = new Setting<>("Dispenser", true, this);
    public Setting<Boolean> pull5 = new Setting<>("Bed", true, this);
    public Setting<Boolean> pull6 = new Setting<>("Spawner", true, this);
    public Setting<Boolean> pull8 = new Setting<>("Beacon", true, this);

    public ESP() {
        super("ESP", Category.RENDER);
        playerColor2 = new Setting<>("PlayerOutline", new HSLColor(1, 54, 43), this);
        playerColor1 = new Setting<>("PlayerColor", new HSLColor(1, 54, 43), this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (Object c : mc.world.loadedTileEntityList) {
            if (pull.getValue()) {
                if (c instanceof TileEntityChest) {
                    RenderUtils.blockESP(((TileEntityChest) c).getPos());
                }}

            if (pull2.getValue()) {
                if (c instanceof TileEntityHopper) {
                    RenderUtils.blockESP(((TileEntityHopper) c).getPos());
                }}

            if (pull3.getValue()) {
                if (c instanceof TileEntityDropper) {
                    RenderUtils.blockESP(((TileEntityDropper) c).getPos());
                }}

            if (pull4.getValue()) {
                if (c instanceof TileEntityDispenser) {
                    RenderUtils.blockESP(((TileEntityDispenser) c).getPos());
                }}

            if (pull5.getValue()) {
                if (c instanceof TileEntityBed) {
                    RenderUtils.blockESP(((TileEntityBed) c).getPos());
                }}

            if (pull6.getValue()) {
                if (c instanceof TileEntityMobSpawner  ) {
                    RenderUtils.blockESP(((TileEntityMobSpawner) c).getPos());
                }}

            if (pull7.getValue()) {
                if (c instanceof TileEntityShulkerBox  ) {
                    RenderUtils.blockESP(((TileEntityShulkerBox) c).getPos());
                }}

            if (pull8.getValue()) {
                if (c instanceof TileEntityBeacon  ) {
                    RenderUtils.blockESP(((TileEntityBeacon) c).getPos());
                }}



            if (pull1.getValue()) {
                if (c instanceof TileEntityEnderChest ) {
                    RenderUtils.blockESP(((TileEntityEnderChest) c).getPos());
                }}

            if (pull10.getValue()) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityItem) {
                        box = new AxisAlignedBB(
                                entity.getEntityBoundingBox().minX
                                        - 0.05
                                        - entity.posX
                                        + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosX),
                                entity.getEntityBoundingBox().minY
                                        - entity.posY
                                        + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosY),
                                entity.getEntityBoundingBox().minZ
                                        - 0.05
                                        - entity.posZ
                                        + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosZ),
                                entity.getEntityBoundingBox().maxX
                                        + 0.05
                                        - entity.posX
                                        + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosX),
                                entity.getEntityBoundingBox().maxY
                                        + 0.1
                                        - entity.posY
                                        + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosY),
                                entity.getEntityBoundingBox().maxZ
                                        + 0.05
                                        - entity.posZ
                                        + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                        .getRenderManager().viewerPosZ));

                        RenderUtils.FillLine(entity, box);
                    }
                }
            }
        }

        if (pull9.getValue()) {
            for (Entity entity : mc.world.playerEntities) {
                if (entity != mc.player && entity != null) {
                    box  = new AxisAlignedBB(
                            entity.getEntityBoundingBox().minX
                                    - 0.05
                                    - entity.posX
                                    + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosX),
                            entity.getEntityBoundingBox().minY
                                    - entity.posY
                                    + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosY),
                            entity.getEntityBoundingBox().minZ
                                    - 0.05
                                    - entity.posZ
                                    + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosZ),
                            entity.getEntityBoundingBox().maxX
                                    + 0.05
                                    - entity.posX
                                    + ((float) ((double) ((float) entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosX),
                            entity.getEntityBoundingBox().maxY
                                    + 0.1
                                    - entity.posY
                                    + ((float) ((double) ((float) entity.lastTickPosY) + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosY),
                            entity.getEntityBoundingBox().maxZ
                                    + 0.05
                                    - entity.posZ
                                    + ((float) ((double) ((float) entity.lastTickPosZ) + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks()) - Minecraft.getMinecraft()
                                    .getRenderManager().viewerPosZ));

                    RenderUtils.FillLine(entity, box);
                }
            }
        }

    }

}

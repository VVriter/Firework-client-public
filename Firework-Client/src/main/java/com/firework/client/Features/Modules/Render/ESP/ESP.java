package com.firework.client.Features.Modules.Render.ESP;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ESP extends Module {
    public Setting<Enum> page = new Setting<>("Page", pages.Storages, this, pages.values());
    public enum pages{
        Storages, Tunnels
    }


    //Tunnels
    public Setting<Boolean> tunnelsBool = new Setting<>("Tunnels", true, this).setVisibility(page,pages.Tunnels);
    public Setting<Double> range = new Setting<>("Range", (double)20, this, 1, 50).setVisibility(page,pages.Tunnels);



    //BoxEspStuff
    public static Setting<Boolean> Items = null;
    public static Setting<Boolean> Chests = null;
    public static Setting<Boolean> EChests = null;
    public static Setting<Boolean> Shulker = null;
    public static Setting<Boolean> Hoper = null;
    public static Setting<Boolean> Dropper = null;
    public static Setting<Boolean> Dispanser = null;
    public static Setting<Boolean> Bed = null;
    public static Setting<Boolean> Spawner = null;
    public static Setting<Boolean> Beacon = null;

    public ESP() {
        super("ESP", Category.RENDER);

        Items = new  Setting<>("Items", true, this).setVisibility(page,pages.Storages);
        Chests = new Setting<>("Chest", true, this).setVisibility(page,pages.Storages);
        Shulker = new Setting<>("Shulker", true, this).setVisibility(page,pages.Storages);
        Hoper = new Setting<>("Hopper", true, this).setVisibility(page,pages.Storages);
        Dropper = new Setting<>("Shulker", true, this).setVisibility(page,pages.Storages);
        Dispanser = new Setting<>("Dispenser", true, this).setVisibility(page,pages.Storages);
        Bed = new Setting<>("Bed", true, this).setVisibility(page,pages.Storages);
        Spawner = new Setting<>("Spawner", true, this).setVisibility(page,pages.Storages);
        Beacon = new Setting<>("Beacon", true, this).setVisibility(page,pages.Storages);
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
      //  StoragedoEsp();

if (tunnelsBool.getValue()){
        int size = this.poses.size();
        for (int i = 0; i < size; ++i) {
            BlockPos posTorender = this.poses.get(i);
            RenderUtils.drawBoxESP(posTorender,new Color(66, 196, 22,255),1,true,true,150,2);
        }
}







        for (Object c : mc.world.loadedTileEntityList) {
            if (Chests.getValue()) {
                if (c instanceof TileEntityChest) {
                    RenderUtils.blockESP(((TileEntityChest) c).getPos());
                }}

            if (Hoper.getValue()) {
                if (c instanceof TileEntityHopper) {
                    RenderUtils.blockESP(((TileEntityHopper) c).getPos());
                }}

            if (Dropper.getValue()) {
                if (c instanceof TileEntityDropper) {
                    RenderUtils.blockESP(((TileEntityDropper) c).getPos());
                }}

            if (Dispanser.getValue()) {
                if (c instanceof TileEntityDispenser) {
                    RenderUtils.blockESP(((TileEntityDispenser) c).getPos());
                }}

            if (Bed.getValue()) {
                if (c instanceof TileEntityBed) {
                    RenderUtils.blockESP(((TileEntityBed) c).getPos());
                }}

            if (Spawner.getValue()) {
                if (c instanceof TileEntityMobSpawner  ) {
                    RenderUtils.blockESP(((TileEntityMobSpawner) c).getPos());
                }}

            if (Shulker.getValue()) {
                if (c instanceof TileEntityShulkerBox  ) {
                    RenderUtils.blockESP(((TileEntityShulkerBox) c).getPos());
                }}

            if (Beacon.getValue()) {
                if (c instanceof TileEntityBeacon  ) {
                    RenderUtils.blockESP(((TileEntityBeacon) c).getPos());
                }}



          /*  if (EChests.getValue()) {
                if (c instanceof TileEntityEnderChest ) {
                    RenderUtils.blockESP(((TileEntityEnderChest) c).getPos());
                }
            } */
        }
    }

    private final List<BlockPos> poses = new ArrayList<>();
    public Vec3d prevPos;

    @Override
    public void onTick() {
        super.onTick();
        if (tunnelsBool.getValue())
            this.update(range.getValue().intValue());
    }

    public void update(int range) {
        this.poses.clear();
        BlockPos player = mc.player.getPosition();
        this.prevPos = mc.player.getPositionVector();

        for (int y = -Math.min(range, player.getY()); y < Math.min(range, 255 - player.getY()); ++y)
        {
            for (int x = -range; x < range; ++x)
            {
                for (int z = -range; z < range; ++z)
                {
                    BlockPos pos = player.add(x, y, z);
                    if (
                            (this.mc.world.getBlockState(pos).getBlock() == Blocks.AIR)
                                    && (this.mc.world.getBlockState(pos.up(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.down(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.up(2)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.north(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.south(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.up(1).north(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.up(1).south(1)).getBlock() == Blocks.AIR)

                                    && (this.mc.world.getBlockState(pos.west(1)).getBlock() == Blocks.AIR)
                                    && (this.mc.world.getBlockState(pos.west(1).up(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).down(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).up(2)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).north(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).south(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).up(1).north(1)).getBlock() == Blocks.AIR)
                                    && !(this.mc.world.getBlockState(pos.west(1).up(1).south(1)).getBlock() == Blocks.AIR)

                                    ||
                                    (this.mc.world.getBlockState(pos).getBlock() == Blocks.AIR)
                                            && (this.mc.world.getBlockState(pos.up(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.down(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.up(2)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.west(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.east(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.up(1).west(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.up(1).east(1)).getBlock() == Blocks.AIR)

                                            && (this.mc.world.getBlockState(pos.north(1)).getBlock() == Blocks.AIR)
                                            && (this.mc.world.getBlockState(pos.north(1).up(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).down(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).up(2)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).west(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).east(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).up(1).west(1)).getBlock() == Blocks.AIR)
                                            && !(this.mc.world.getBlockState(pos.north(1).up(1).east(1)).getBlock() == Blocks.AIR)) {
                        this.poses.add(pos);
                    }
                }
            }
        }
    }


        @Override
        public void onDisable() {
            super.onDisable();
            this.poses.clear();
        }
}

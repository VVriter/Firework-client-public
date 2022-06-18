package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.YawUtil;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ModuleArgs(name = "BaseFinder",category = Module.Category.WORLD)
public class BaseFinder extends Module {

    private final Set<Entity> donkey = new HashSet< Entity >( );
    public Setting<Enum> huntingMode = new Setting<>("HuntingMode",huntingMode1.Normal,this,huntingMode1.values());
    public Setting<Boolean> isSprint = new Setting<>("Sprint", true, this);
    public Setting<Boolean> sounds = new Setting<>("Sounds", true, this);

    public Setting<Double> shulkers = new Setting<>("Shulkers", (double)1, this, 0, 100);
    public Setting<Double> chests = new Setting<>("Chests", (double)6, this, 0, 100);
    public Setting<Double> echests = new Setting<>("EnderChests", (double)2, this, 0, 100);
    public Setting<Double> frames = new Setting<>("EnderChests", (double)1, this, 0, 100);
    public Setting<Double> donkeys = new Setting<>("EnderChests", (double)1, this, 0, 100);

    public Setting<Boolean> bed = new Setting<>("Sounds", true, this);
    public BlockPos pos;
    public BlockPos posNow;
    public int blocksUWalkedX;
    public int blocksUWalkedZ;
    public int blocksUwalked;
    public int blocksUMustToWalk;



    int Shulkers;
    int Chests;
    int Echests;
    int Frames;
    int Donkeys;



    @Override
    public void onEnable(){
        super.onEnable();
        this.donkey.clear();
        pos = mc.player.getPosition();
        if(huntingMode.getValue(huntingMode1.Normal)){
            mc.player.rotationYaw = 0;
            blocksUMustToWalk = 20;
        }
    }


    @Override
    public void onTick(){
        super.onTick();







        if(huntingMode.getValue(huntingMode1.Normal)){
            YawUtil.MakeRoundedYaw(40);
            mc.player.setSprinting(isSprint.getValue());
        }

        if(huntingMode.getValue(huntingMode1.Spiral)){
            YawUtil.MakeRoundedYaw(40);
            mc.player.setSprinting(isSprint.getValue());
        }

        System.out.println(blocksUwalked);

        posNow = mc.player.getPosition();
        blocksUWalkedX = pos.getX() - posNow.getX();
        blocksUWalkedZ = pos.getZ() - posNow.getZ();
        blocksUwalked = Math.abs(blocksUWalkedX+blocksUWalkedZ);



        if(huntingMode.getValue(huntingMode1.Spiral)){
            if(blocksUwalked == blocksUMustToWalk){
                if(mc.player.rotationYaw == 0){
                    this.blocksUMustToWalk = blocksUMustToWalk + 20;
                    this.pos = mc.player.getPosition();
                    mc.player.rotationYaw = 90;
                }else if(mc.player.rotationYaw == 90){
                    this.pos = mc.player.getPosition();
                    mc.player.rotationYaw = 180;
                }else if(mc.player.rotationYaw == 180){
                    this.blocksUMustToWalk = blocksUMustToWalk + 20;
                    this.pos = mc.player.getPosition();
                    mc.player.rotationYaw = -90;
                }else if(mc.player.rotationYaw == -90){
                    this.pos = mc.player.getPosition();
                    mc.player.rotationYaw = 0;
                }
            }
        }
    }



    @SubscribeEvent
    public void onTitleEntity(RenderWorldLastEvent e){
        for (Object c: mc.world.loadedTileEntityList) {
            if(c instanceof TileEntityChest){
                Chests++;
                if(Chests >= chests.getValue().intValue()){
                    MessageUtil.sendClientMessage("Founded "+Chests+" chests near you!",true);
                }
            }if(c instanceof TileEntityEnderChest){
                Echests++;
                if(Echests >= echests.getValue().intValue()){
                    MessageUtil.sendClientMessage("Founded "+Echests+" EnderChests near you!",true);
                }
            }if(c instanceof TileEntityShulkerBox){
                Shulkers++;
                if(Shulkers >= shulkers.getValue().intValue()){
                    MessageUtil.sendClientMessage("Founded "+Shulkers+" shulkers near you!",true);
                }if(c instanceof TileEntityBed){
                    MessageUtil.sendClientMessage("Founded bed near you",true);
                }
            }
        }
    }


    @SubscribeEvent
    public void onChunkDataEvent(ChunkDataEvent e){

    }
    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        if(huntingMode.getValue(huntingMode1.Normal) && huntingMode.getValue((huntingMode1.Spiral))){
            event.getMovementInput().moveForward = 1.0f;
        }
    }


    public enum huntingMode1{
        Normal, Spiral, None
    }
}

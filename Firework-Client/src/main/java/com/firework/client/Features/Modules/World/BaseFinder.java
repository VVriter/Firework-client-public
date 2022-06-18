package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.YawUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@ModuleArgs(name = "BaseFinder",category = Module.Category.WORLD)
public class BaseFinder extends Module {

    public Setting<String> huntingMode = new Setting<>("HuntingMode", "Normal", this, Arrays.asList("Normal", "Spiral"));
    public Setting<Boolean> isSprint = new Setting<>("Sprint", true, this);

    public BlockPos pos;
    public BlockPos posNow;
    public int blocksUWalkedX;
    public int blocksUWalkedZ;
    public int blocksUwalked;
    public int blocksUMustToWalk;

    @Override
    public void onEnable(){
        super.onEnable();
        pos = mc.player.getPosition();
        if(huntingMode.getValue("Spiral")){
            mc.player.rotationYaw = 0;
            blocksUMustToWalk = 20;
        }
    }


    @Override
    public void onTick(){
        super.onTick();

        if(huntingMode.getValue("Normal")){
            YawUtil.MakeRoundedYaw();
            mc.player.setSprinting(isSprint.getValue());
        }

        if(huntingMode.getValue("Spiral")){
            YawUtil.MakeRoundedYaw();
            mc.player.setSprinting(isSprint.getValue());
        }

        System.out.println(blocksUwalked);

        posNow = mc.player.getPosition();
        blocksUWalkedX = pos.getX() - posNow.getX();
        blocksUWalkedZ = pos.getZ() - posNow.getZ();
        blocksUwalked = Math.abs(blocksUWalkedX+blocksUWalkedZ);



        if(huntingMode.getValue("Spiral")){
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
    public void onUpdateInput(InputUpdateEvent event) {
            event.getMovementInput().moveForward = 1.0f;
    }
}

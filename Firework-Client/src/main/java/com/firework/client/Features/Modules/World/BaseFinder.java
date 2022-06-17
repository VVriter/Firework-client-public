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

    public float yaw;

    @Override
    public void onEnable(){
        super.onEnable();
        pos = mc.player.getPosition();
        yaw = mc.player.rotationYaw;
        if(huntingMode.getValue("Spiral")){
            yaw = 0;
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


        posNow = mc.player.getPosition();
        blocksUWalkedX = pos.getX() - posNow.getX();
        blocksUWalkedZ = pos.getZ() - posNow.getZ();
        blocksUwalked = Math.abs(blocksUWalkedX+blocksUWalkedZ);

        if(huntingMode.getValue("Spiral")){
            if(blocksUwalked == blocksUMustToWalk){
                if(yaw == 0){
                    mc.player.rotationYaw = yaw + 90;
                }else if(yaw == 90){
                    mc.player.rotationYaw = 180;
                }else if(yaw == 180){
                    mc.player.rotationYaw = -90;
                }else if(yaw == -90){
                    mc.player.rotationYaw = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        if(huntingMode.getValue("Normal"))
            event.getMovementInput().moveForward = 1.0f;
    }
}

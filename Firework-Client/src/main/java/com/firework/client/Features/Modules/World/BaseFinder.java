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

    public Setting<String> HuntingMode = new Setting<>("HuntingMode", "Normal", this, Arrays.asList("Normal", "Spiral"));
    public Setting<Boolean> isSprint = new Setting<>("Sprint", true, this);

    BlockPos pos;
    BlockPos posNow;

    int blocksUWalkedX;
    int blocksUWalkedZ;
    int blocksUwalked;
    int blocksUMustToWalk;

    float yaw = mc.player.rotationYaw;

    @Override
    public void onEnable(){
        super.onEnable();
        pos = mc.player.getPosition();
        if(HuntingMode.getValue().equals("Spiral")){
            yaw = 0;
            blocksUMustToWalk = 20;
        }
    }


    @Override
    public void onTick(){
        super.onTick();

        if(HuntingMode.getValue().equals("Normal")){
            YawUtil.MakeRoundedYaw();
            mc.player.setSprinting(isSprint.getValue());
        }


        posNow = mc.player.getPosition();
        blocksUWalkedX = pos.getX() - posNow.getX();
        blocksUWalkedZ = pos.getZ() - posNow.getZ();
        blocksUwalked = Math.abs(blocksUWalkedX+blocksUWalkedZ);

        if(HuntingMode.getValue().equals("Spiral")){
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

    //Это для мода нормал, оно просто будет идти вперед (или же лететь на элитре)
    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        if(HuntingMode.getValue().equals("Normal"))
            event.getMovementInput().moveForward = 1.0f;
    }
}

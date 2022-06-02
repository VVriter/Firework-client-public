package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;

import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;


@ModuleArgs(name = "AutoFish",category = Module.Category.MISC)
public class AutoFish extends Module {
    public  Setting<Boolean> enabled = this.isEnabled;


    public Setting<String> mode  = new Setting<>("Mode", "Normal", this, Arrays.asList("Normal","Advanced"));
    public Setting<String> swith  = new Setting<>("Switch", "Normal", this, Arrays.asList("Normal","Silent","None"));
    public Setting<Boolean> swing  = new Setting<>("Swing", true, this);

    @Override
    public void onEnable(){
        super.onEnable();
        if(!mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD) && swith.getValue().equals("Normal")){
            makeNormalSwitch();
        }else {
            if((mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD))))){
            MessageUtil.sendError("You need to hold Fishing rod in mainhand!",-1117);
            enabled.setValue(false);
            }
        }
    }


    @Override
    public void onTick(){
        super.onTick();
        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }


        if(swith.getValue().equals("Normal")){
            makeNormalSwitch();
        }else{
            System.out.println("silent");
        }
    }










    public void makeNormalSwitch(){
        if ( (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD)) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemFishingRod) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }
         }

}

package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishingRod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
        if(mode.getValue().equals("Advanced")){
            sendEnabledMsg();
        }
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








        //Atm swing animations
        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }

        //Mode
        if(mode.getValue().equals("Advanced")){
            putIsWebhookLinked();
            if(isWebhookPresent){
                //AutoFishCode



            }else{
                MessageUtil.sendError("Webhook is not present, use "+ CommandManager.prefix+"webhook to set webhook link",-1117);
                enabled.setValue(false);
            }
        }


        //Switch
        if(swith.getValue().equals("Normal")){
            makeNormalSwitch();
        }else if(swith.getValue().equals("Silent")){
            System.out.println("silent");
        }else {
            if((mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD))))){
            MessageUtil.sendError("You need to hold Fishing rod in mainhand!",-1117);
            enabled.setValue(false);}
        }
    }


    @SubscribeEvent
    public void onItemPickUp(PlayerEvent.ItemPickupEvent e){
        if(mode.getValue().equals("Advanced")){
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordUtil.sendMsg("```You picked up an item: "+e.getStack().getItem().getRegistryName()+"```",DiscordNotificator.webhook);
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
        }
    }



    public boolean isWebhookPresent = false;


    public void putIsWebhookLinked() {
        isWebhookPresent = !DiscordNotificator.webhook.equals("");
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

         public void sendEnabledMsg(){
             new Thread(
                     new Runnable() {
                         public void run() {
                             try {
                                 DiscordUtil.sendMsg("```AutoFish module mod advenced is enabled!```", DiscordNotificator.webhook);
                             }catch (Exception e){
                                 MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                             }
                         }
                     }).start();
         }
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleManifest(name = "HoleFiller",category = Module.Category.COMBAT)
public class HoleFiller extends Module {


    public Setting<Enum> mode = new Setting<>("Mode", modes.Normal, this, modes.values());
    public enum modes{
        Normal, Smart
    }
    public Setting<Double> distance = new Setting<>("Distance", (double)1, this, 1, 10);

    public Setting<Double> delay = new Setting<>("Delay", (double)1, this, 1, 10);
    
    public Setting<Boolean> shuldDisableOnJump = new Setting<>("DisableOnJump", true, this);


    public Setting<HSLColor> renderColor = new Setting<>("RenderColor", new HSLColor(1, 54, 43), this);



    @Override
    public void onTick(){
        super.onTick();
        if(mode.getValue(modes.Normal)){
            makeHoleFill();
        }else if(mode.getValue(modes.Smart)){
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityPlayer && e != mc.player) {
                    if (mc.player.getDistance(e) <= distance.getValue() && mc.player.getDistance(e) <= distance.getValue() ) {
                        makeHoleFill();
                    }
                }
            }
        }
    }



    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shuldDisableOnJump.getValue()){onDisable();}
        }
    }



    public void makeHoleFill(){
        //HoleFillCode
        MessageUtil.sendClientMessage("Im Holefiling now",-1117);
    }
}

package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Announcer extends Module {

    public Setting<Boolean> join = new Setting<>("Join", false, this);
    public Setting<Boolean> leave = new Setting<>("Leave", false, this);
    public Setting<Boolean> food = new Setting<>("Food", false, this);
    public Setting<Boolean> place = new Setting<>("Place", false, this);
    public Setting<Boolean> Break = new Setting<>("Break", false, this);
    public Setting<Boolean> worldTime = new Setting<>("WorldTime", false, this);
    public Setting<Boolean> clientSideOnly = new Setting<>("ClientSideOnly", false, this);




    public Announcer(){ super("Announcer", Category.CHAT); }


    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            MessageUtil.sendClientMessage(e.getEntity().getName()+"Joinde to the game!",false);
        }
    }

}

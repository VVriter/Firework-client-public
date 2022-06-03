package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;

@ModuleArgs(name = "AutoLog",category = Module.Category.MISC)
public class AutoLog extends Module {

    static String gegra = "";

    public  Setting<Boolean> enabled = this.isEnabled;
    public Setting<Double> helth = new Setting<>("Health", (double)5, this, 1, 20);
    public Setting<Boolean> disable = new Setting<>("AutoDisable", false, this);


    public void onDamage(DamageSource e){
        String bebra = e.getImmediateSource().getName();
        gegra = bebra;
    }


    @Override
    public void onTick(){
        super.onTick();
        if(mc.player == null || mc.world == null) return;
        int health = (int) helth.getValue().intValue();
        if(mc.player.getHealth() < health) {
            mc.player.connection.handleDisconnect(new SPacketDisconnect(new TextComponentString("Autolog!")));
            if(disable.getValue()){
            enabled.setValue(false);}
        }
    }
}

package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import net.minecraft.util.ResourceLocation;

public class Notifications extends Module {

        public static Setting<Boolean> enabled = null;
        public static Setting<Boolean> sounds = null;


    public Notifications(){super("Notifications",Category.CLIENT);
        enabled = this.isEnabled;
        this.isEnabled.setValue(true);
        sounds = new Setting<>("Sounds", true, this);}


    public static void notificate(){
        if(Notifications.sounds.getValue() && Notifications.enabled.getValue()){
            SoundUtill.playSound(new ResourceLocation("firework/audio/pop.wav"));}
    }
}

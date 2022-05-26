package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import net.minecraft.util.ResourceLocation;

public class Narrator extends Module{
    public Narrator(){super("Narrator",Category.CHAT);}

    @Override
    public void onEnable(){
        super.onEnable();
        SoundUtill.playSound(new ResourceLocation("firework/audio/hello.wav"));
    }


    @Override
    public void onDisable(){
        super.onDisable();
        SoundUtill.playSound(new ResourceLocation("firework/audio/bye.wav"));
    }
}
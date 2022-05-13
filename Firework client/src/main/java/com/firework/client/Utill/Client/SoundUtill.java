package com.firework.client.Utill.Client;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundUtill {

    //Utilite to play sounds in minecraft | Using:  SoundUtill.playSound(new ResourceLocation("audio/file.wav"));
    public static void playSound(ResourceLocation rl) {
        try {
            InputStream sound = Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream();
            AudioStream as = new AudioStream(sound);
            AudioPlayer.player.start(as);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
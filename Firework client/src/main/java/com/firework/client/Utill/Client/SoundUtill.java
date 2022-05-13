package com.firework.client.Utill.Client;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundUtill {

    //Utilite to play sounds in minecraft | Using:  SoundUtill.playSound(new ResourceLocation("audio/file.wav"));
    public static void playSound(ResourceLocation rl) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream());
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
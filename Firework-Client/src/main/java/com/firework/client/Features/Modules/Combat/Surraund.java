package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class Surraund extends Module {

        public static Setting<Boolean> autoCenter = null;

    public Surraund(){super("Surraund",Category.COMBAT);

        autoCenter = new Setting<>("AutoCenter", false, this);}



    public void onEnable(){
        super.onEnable();
        System.out.println(Math.round(Minecraft.getMinecraft().player.getPosition().getX())+0.5);
        System.out.println( Math.round(Minecraft.getMinecraft().player.getPosition().getY()));
        System.out.println(Math.round(Minecraft.getMinecraft().player.getPosition().getZ())+0.5);
        Minecraft.getMinecraft().player.setPosition(Math.round(Minecraft.getMinecraft().player.getPosition().getX())+0.5,Math.round(Minecraft.getMinecraft().player.getPosition().getY()),Math.round(Minecraft.getMinecraft().player.getPosition().getZ())+0.5);
    }



}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticlesESP extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();

    public Setting<Boolean> pull8 = new Setting<>("Heart", false, this);
    public Setting<Boolean> pull  = new Setting<>("Cloud", false, this);
    public Setting<Boolean> pull1  = new Setting<>("Flame", false, this);
    public Setting<Boolean> pull7  = new Setting<>("Smoke", false, this);
    public Setting<Boolean> pull2  = new Setting<>("RedStone", false, this);
    public Setting<Boolean> pull3  = new Setting<>("FireWork", false, this);
    public Setting<Boolean> pull4  = new Setting<>("Portal", false, this);
    public Setting<Boolean> pull5   = new Setting<>("Spit", false, this);
    public Setting<Boolean> b1= new Setting<>("Slime", false, this);
    public Setting<Boolean> b2 = new Setting<>("DragonBreath", false, this);
    public Setting<Boolean> b3  = new Setting<>("EndRod", false, this);
    public Setting<Boolean> b4  = new Setting<>("Totem", false, this);
    public Setting<Boolean> b5   = new Setting<>("Bubble", false, this);
    public Setting<Boolean> b6   = new Setting<>("SnowBall", false, this);


    public ParticlesESP(){super("ParticlesESP",Category.RENDER);}


    @SubscribeEvent
    public void tryToExecute() {
        super.tryToExecute();

        if (b6.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SNOWBALL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (b5.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (b4.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.TOTEM, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (b3.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.END_ROD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (b2.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (b1.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull8.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.HEART, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.CLOUD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull1.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull7.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull2.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.3, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.4, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull3.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull4.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (pull5.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SPIT, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

}

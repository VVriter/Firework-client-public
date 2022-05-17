package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticlesESP extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();

    public Setting<Boolean> Heart = new Setting<>("Heart", false, this);
    public Setting<Boolean> Cloud  = new Setting<>("Cloud", false, this);
    public Setting<Boolean> Flame  = new Setting<>("Flame", false, this);
    public Setting<Boolean> Smoke  = new Setting<>("Smoke", false, this);
    public Setting<Boolean> RedStone  = new Setting<>("RedStone", false, this);
    public Setting<Boolean> FireWork  = new Setting<>("FireWork", false, this);
    public Setting<Boolean> Portal  = new Setting<>("Portal", false, this);
    public Setting<Boolean> Spit   = new Setting<>("Spit", false, this);
    public Setting<Boolean> Slime= new Setting<>("Slime", false, this);
    public Setting<Boolean> DragonBreath = new Setting<>("DragonBreath", false, this);
    public Setting<Boolean> EndRod  = new Setting<>("EndRod", false, this);
    public Setting<Boolean> Totem  = new Setting<>("Totem", false, this);
    public Setting<Boolean> Bubble   = new Setting<>("Bubble", false, this);
    public Setting<Boolean> SnowBall   = new Setting<>("SnowBall", false, this);


    public ParticlesESP(){super("ParticlesESP",Category.RENDER);}


    @SubscribeEvent
    public void tryToExecute() {
        super.tryToExecute();

        if (SnowBall.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SNOWBALL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Bubble.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Totem.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.TOTEM, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (EndRod.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.END_ROD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (DragonBreath.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Slime.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Heart.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.HEART, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Cloud.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.CLOUD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Flame.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Smoke.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (RedStone.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.3, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.4, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (FireWork.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Portal.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Spit.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SPIT, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

}

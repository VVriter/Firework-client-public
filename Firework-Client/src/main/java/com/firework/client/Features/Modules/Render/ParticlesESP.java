package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticlesESP extends Module {

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
    public Setting<Boolean> SnowBall   = new Setting<>("SnowBall", false, this);


    public ParticlesESP(){super("ParticlesESP",Category.RENDER);}


    @SubscribeEvent
    public void tryToExecute() {
        super.tryToExecute();

        if (SnowBall.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SNOWBALL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Totem.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.TOTEM, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (EndRod.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.END_ROD, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (DragonBreath.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.1, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);}
        if (Slime.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SLIME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.1, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SLIME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SLIME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.3, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SLIME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.4, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SLIME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.5, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);   }
        if (Heart.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.HEART, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Cloud.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.CLOUD, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Flame.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FLAME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.1, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FLAME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FLAME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.3, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FLAME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.4, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FLAME, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.5, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]); }
        if (Smoke.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (RedStone.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.REDSTONE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.1, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.REDSTONE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.REDSTONE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.3, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.REDSTONE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.4, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.REDSTONE, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.5, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (FireWork.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.5, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Portal.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (Spit.getValue()) {
            Firework.minecraft.world.spawnParticle(EnumParticleTypes.SPIT, Firework.minecraft.player.posX, Firework.minecraft.player.posY + 0.2, Firework.minecraft.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

}

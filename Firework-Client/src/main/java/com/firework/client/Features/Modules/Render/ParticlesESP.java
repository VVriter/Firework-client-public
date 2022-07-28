package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

@ModuleManifest(
        name = "ParticlesBredCrams",
        category = Module.Category.VISUALS,
        description = "Cool render module"
)
public class ParticlesESP extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();

    public Setting<Boolean> heart = new Setting<>("Heart", false, this);
    public Setting<Boolean> cloud = new Setting<>("Cloud", false, this);
    public Setting<Boolean> flame = new Setting<>("Flame", true, this);
    public Setting<Boolean> smoke = new Setting<>("Smoke", false, this);
    public Setting<Boolean> redStone = new Setting<>("RedStone", false, this);
    public Setting<Boolean> firework = new Setting<>("Firework", false, this);
    public Setting<Boolean> portal = new Setting<>("Portal", false, this);
    public Setting<Boolean> spit = new Setting<>("Spit", false, this);
    public Setting<Boolean> slime = new Setting<>("Slime", false, this);
    public Setting<Boolean> dragonBreath = new Setting<>("DragonBreath", false, this);
    public Setting<Boolean> endRod = new Setting<>("EndRod", false, this);
    public Setting<Boolean> totem = new Setting<>("Totem", false, this);
    public Setting<Boolean> snowBall = new Setting<>("SnowBall", false, this);

    @Override
    public void onTick() {

        if (snowBall.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SNOWBALL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (totem.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.TOTEM, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (endRod.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.END_ROD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (dragonBreath.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);}
        if (slime.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.3, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.4, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SLIME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);   }
        if (heart.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.HEART, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (cloud.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.CLOUD, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (flame.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.3, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.4, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FLAME, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]); }
        if (smoke.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (redStone.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.1, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.3, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.4, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (firework.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.5, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (portal.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.PORTAL, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
        if (spit.getValue()) {
            ParticlesESP.mc.world.spawnParticle(EnumParticleTypes.SPIT, ParticlesESP.mc.player.posX, ParticlesESP.mc.player.posY + 0.2, ParticlesESP.mc.player.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

}

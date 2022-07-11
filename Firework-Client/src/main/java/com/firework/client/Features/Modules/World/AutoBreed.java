package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

@ModuleManifest(name = "AutoBread",category = Module.Category.WORLD)
public class AutoBreed extends Module {

    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);

    @Override
    public void onUpdate() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != null && entity instanceof EntityAnimal) {
                EntityAnimal entityAnimal = (EntityAnimal)entity;
                if (entityAnimal.getHealth() > 0.0F && !entityAnimal.isChild() && !entityAnimal.isInLove())
                    if (EntityUtil.getDistanceFromEntityToEntity(entityAnimal) <= 5 && entityAnimal.isBreedingItem(mc.player.inventory.getCurrentItem())) {
                        mc.playerController.interactWithEntity((EntityPlayer)mc.player, (Entity)entityAnimal, EnumHand.MAIN_HAND);
                        if (rotate.getValue()) {
                            float[] arrayOfFloat = getRotationsToward((Entity)entityAnimal);
                            float f1 = getSensitivityMultiplier();
                            float f2 = Math.round(arrayOfFloat[0] / f1) * f1;
                            float f3 = Math.round(arrayOfFloat[1] / f1) * f1;
                            mc.player.rotationYaw = f2;
                            mc.player.rotationPitch = f3;
                            mc.player.rotationYawHead = f2;
                            mc.player.renderYawOffset = f2;
                        }
                    }
            }
        }
    }

    private float getSensitivityMultiplier() {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return f * f * f * 8.0F * 0.15F;
    }

    private float[] getRotationsToward(Entity paramEntity) {
        double d1 = paramEntity.posX - mc.player.posX;
        double d2 = paramEntity.posY + paramEntity.getEyeHeight() - mc.player.posY + mc.player.getEyeHeight();
        double d3 = paramEntity.posZ - mc.player.posZ;
        double d4 = MathHelper.sqrt(d1 * d1 + d3 * d3);
        float f1 = fixRotation(mc.player.rotationYaw, (float)(MathHelper.atan2(d3, d1) * 180.0D / Math.PI) - 90.0F, 360.0F);
        float f2 = fixRotation(mc.player.rotationPitch, (float)-(MathHelper.atan2(d2, d4) * 180.0D / Math.PI), 360.0F);
        return new float[] { f1, f2 };
    }



    private float fixRotation(float paramFloat1, float paramFloat2, float paramFloat3) {
        float f = MathHelper.wrapDegrees(paramFloat2 - paramFloat1);
        if (f > paramFloat3)
            f = paramFloat3;
        if (f < -paramFloat3)
            f = -paramFloat3;
        return paramFloat1 + f;
    }


}


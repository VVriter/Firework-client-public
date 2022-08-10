package com.firework.client.Implementations.Utill.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;


import static com.firework.client.Implementations.Utill.Entity.MovementUtil.mc;

public class TargetUtil {

    public static Entity getClosest(boolean player, boolean animals, boolean mobs, double maxRange) {
        double lowestDistance = Integer.MAX_VALUE;
        Entity closest = null;

            for (Entity entity : mc.world.loadedEntityList) {
                if (entity.getName() == mc.getSession().getUsername()) continue;
                if (entity instanceof EntityMob && mobs) {
                if (entity.getDistance(mc.player) < lowestDistance) {
                    if(entity.getPositionVector().distanceTo(mc.player.getPositionVector()) <= maxRange) {
                        lowestDistance = entity.getDistance(mc.player);
                        closest = entity;
                        }
                    }
                }

                if (entity instanceof EntityAnimal && animals) {
                    if (entity.getDistance(mc.player) < lowestDistance) {
                        if(entity.getPositionVector().distanceTo(mc.player.getPositionVector()) <= maxRange) {
                            lowestDistance = entity.getDistance(mc.player);
                            closest = entity;
                        }
                    }
                }

                if (entity instanceof EntityPlayer && player) {
                    if (entity.getDistance(mc.player) < lowestDistance) {
                        if(entity.getPositionVector().distanceTo(mc.player.getPositionVector()) <= maxRange) {
                            lowestDistance = entity.getDistance(mc.player);
                            closest = entity;
                        }
                    }
                }
            }

            return closest;
    }


}

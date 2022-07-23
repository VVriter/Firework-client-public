package com.firework.client.Implementations.Utill;

import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.Triple;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class CrystalUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static BlockPos bestCrystalPos(EntityPlayer target, final int range, final boolean legal){
        //BlockPos | SelfDamage | TargetDamage
        Triple<BlockPos, Float, Float> bestPosition = null;
        for(BlockPos pos : BlockUtil.getSphere(range, true)){
            if(CrystalUtil.canPlaceCrystal(pos, legal)) {
                if (bestPosition == null) {
                    float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                    float targetDamage = CrystalUtil.calculateDamage(pos, target);
                    bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                } else {
                    float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                    float targetDamage = CrystalUtil.calculateDamage(pos, target);
                    if (targetDamage - selfDamage > bestPosition.three - bestPosition.two) {
                        bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                    }
                }
            }
        }

        return bestPosition.one;
    }

    public static EntityEnderCrystal getBestCrystal(EntityPlayer target, final int range){
        //Crystal | SelfDamage | TargetDamage
        Triple<EntityEnderCrystal, Float, Float> bestCrystal = new Triple<>(null, 0f, 0f);
        for(EntityEnderCrystal entity : CrystalUtil.getCrystals(range)){
            if (bestCrystal == null) {
                float selfDamage = CrystalUtil.calculateDamage(entity.getPosition(), mc.player)/2;
                float targetDamage = CrystalUtil.calculateDamage(entity.getPosition(), target);
                bestCrystal = new Triple<>(entity, selfDamage, targetDamage);
            } else {
                float selfDamage = CrystalUtil.calculateDamage(entity.getPosition(), mc.player)/2;
                float targetDamage = CrystalUtil.calculateDamage(entity.getPosition(), target);
                if (targetDamage - selfDamage > bestCrystal.three - bestCrystal.two) {
                    bestCrystal = new Triple<>(entity, selfDamage, targetDamage);
                }
            }
        }

        return bestCrystal.one;
    }

    public static BlockPos bestCrystalPos(EntityPlayer target, final int range, final boolean legal, final float maxSelfDamage, final float minTargetDamage){
        //BlockPos | SelfDamage | TargetDamage
        Triple<BlockPos, Float, Float> bestPosition = null;
        for(BlockPos pos : BlockUtil.getSphere(range, true)){
            if(CrystalUtil.canPlaceCrystal(pos, legal)) {
                if (bestPosition == null) {
                    float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                    float targetDamage = CrystalUtil.calculateDamage(pos, target);
                    if(selfDamage > maxSelfDamage || targetDamage < minTargetDamage) continue;
                    bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                } else {
                    float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                    float targetDamage = CrystalUtil.calculateDamage(pos, target);
                    if(selfDamage > maxSelfDamage || targetDamage < minTargetDamage) continue;
                    if (targetDamage - selfDamage > bestPosition.three - bestPosition.two) {
                        bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                    }
                }
            }
        }

        return bestPosition.one;
    }

    public static EntityEnderCrystal getCrystalAtPos(BlockPos pos) {
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0)))) {
            if (entity instanceof EntityEnderCrystal && entity.isEntityAlive()) {
                return (EntityEnderCrystal)entity;
            }
        }

        return null;
    }
}
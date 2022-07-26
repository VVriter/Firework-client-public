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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class CrystalUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static EntityEnderCrystal getBestCrystal(EntityPlayer target, final int range, final boolean rayTraceCheck){
        //Crystal | SelfDamage | TargetDamage
        Triple<EntityEnderCrystal, Float, Float> bestCrystal = new Triple<>(null, 0f, 0f);
        for(EntityEnderCrystal entity : CrystalUtil.getCrystals(range)){
            if(!mc.player.canEntityBeSeen(entity) && rayTraceCheck) continue;
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

    public static BlockPos bestCrystalPos(EntityPlayer target, final int range, final boolean legal, final float maxSelfDamage, final float minTargetDamage, final boolean rayTraceCheck){
        //BlockPos | SelfDamage | TargetDamage
        Triple<BlockPos, Float, Float> bestPosition = new Triple<>(null, 0f, 0f);
        for(BlockPos pos : BlockUtil.getSphere(range, true)){
            RayTraceResult result = mc.world.rayTraceBlocks(
                    new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
                    new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5,pos.getZ() + 0.5));
            if(result != null && result.hitVec != null) {
                if (CrystalUtil.canPlaceCrystal(pos, legal)) {
                    if (bestPosition == new Triple<BlockPos, Float, Float>(null, 0f, 0f)) {
                        float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                        float targetDamage = CrystalUtil.calculateDamage(pos, target);
                        if (selfDamage > maxSelfDamage || targetDamage < minTargetDamage) continue;
                        bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                    } else {
                        float selfDamage = CrystalUtil.calculateDamage(pos, mc.player);
                        float targetDamage = CrystalUtil.calculateDamage(pos, target);
                        if (selfDamage > maxSelfDamage || targetDamage < minTargetDamage) continue;
                        if (targetDamage - selfDamage > bestPosition.three - bestPosition.two) {
                            bestPosition = new Triple<>(pos, selfDamage, targetDamage);
                        }
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

    public static boolean canVectorBeSeen(Vec3d vec3d) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ), vec3d, false, true, false) == null;
    }
}

package com.firework.client.Features.Modules.Combat.Rewrite;

import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.Triple;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.Util.mc;

public class CrystalUtils {
    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = CrystalUtil.getBlastReduction((EntityLivingBase)entity, CrystalUtil.getDamageMultiplied(damage), new Explosion((World)mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(Entity crystal, Entity entity) {
        return CrystalUtil.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    public static float calculateDamage(BlockPos pos, Entity entity) {
        return CrystalUtil.calculateDamage((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5, entity);
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        Block block = BlockUtil.getBlock(pos);

        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();

            if (floor == Blocks.AIR && ceil == Blocks.AIR) {
                for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0)))) {
                    if (!entity.isDead) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static ArrayList<EntityEnderCrystal> getCrystals(double distance) {
        ArrayList<EntityEnderCrystal> list = new ArrayList<EntityEnderCrystal>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                if (entity.getDistance(mc.player) <= distance) {
                    list.add((EntityEnderCrystal)entity);
                }
            }
        }

        return list;
    }

    public static EntityEnderCrystal getBestCrystal(EntityPlayer target, final int range, final float maxSelfDamage, final float minTargetDamage){
        if(target == null) return null;
        EntityEnderCrystal bestCrystal = null;
        float selfDamage = 0;
        float targetDamage = 0;
        for(EntityEnderCrystal entity : getCrystals(range)){
            if (bestCrystal == null) {
                float tempSelfDamage = calculateDamage(entity.getPosition(), mc.player)/2;
                float tempTargetDamage = calculateDamage(entity.getPosition(), target);
                if (tempSelfDamage > maxSelfDamage || tempTargetDamage < minTargetDamage) continue;
                bestCrystal = entity;
                selfDamage = tempSelfDamage;
                targetDamage = tempTargetDamage;
            } else {
                float tempSelfDamage = calculateDamage(entity.getPosition(), mc.player)/2;
                float tempTargetDamage = calculateDamage(entity.getPosition(), target);
                if (tempSelfDamage > maxSelfDamage || tempTargetDamage < minTargetDamage) continue;
                if (targetDamage - selfDamage > targetDamage - tempSelfDamage) {
                    bestCrystal = entity;
                    selfDamage = tempSelfDamage;
                    targetDamage = tempTargetDamage;
                }
            }
        }

        return bestCrystal;
    }

    public static BlockPos bestCrystalPos(EntityPlayer target, final int range, final float maxSelfDamage, final float minTargetDamage){
        if(target == null) return null;
        BlockPos bestPosition = null;
        float selfDamage = 0;
        float targetDamage = 0;
        for(BlockPos pos : BlockUtil.getSphere(range, true)){
            if (canPlaceCrystal(pos)) {
                if (bestPosition == null) {
                    float localSelfDamage = calculateDamage(pos, mc.player);
                    float localTargetDamage = calculateDamage(pos, target);
                    if (localSelfDamage > maxSelfDamage || localTargetDamage < minTargetDamage) continue;
                    bestPosition = pos;
                    selfDamage = localSelfDamage;
                    targetDamage = localTargetDamage;
                } else {
                    float localSelfDamage = calculateDamage(pos, mc.player);
                    float localTargetDamage = calculateDamage(pos, target);
                    if (localSelfDamage >= maxSelfDamage || localTargetDamage <= minTargetDamage) continue;
                    if (localTargetDamage - localSelfDamage > targetDamage - selfDamage) {
                        bestPosition = pos;
                        selfDamage = localSelfDamage;
                        targetDamage = localTargetDamage;
                    }
                }
            }
        }

        return bestPosition;
    }

}

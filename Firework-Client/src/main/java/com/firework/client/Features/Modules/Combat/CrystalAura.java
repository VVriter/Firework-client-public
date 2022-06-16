package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

import static java.lang.Math.*;

@ModuleArgs(name = "CrystalAura", category = Module.Category.COMBAT)
public class CrystalAura extends Module{
    public Setting<EnumHand> hand = new Setting<>("Hand", EnumHand.MAIN_HAND, this, EnumHand.values());
    public Setting<Double> targetRange = new Setting<>("TargetRange", 10d, this, 0, 15);

    public Setting<Double> placeRange = new Setting<>("PlaceRange", 6d, this, 0, 6);
    public Setting<Double> breakRange = new Setting<>("BreakRange", 6d, this, 0, 6);

    public Setting<Double> maxSelfDamage = new Setting<>("MaxSelfDamage", 6d, this, 0, 36);

    public ArrayList<BlockPos> crystalPlaceableBlocks = new ArrayList<>();

    public ArrayList<BlockPos> crystalBlocks = new ArrayList<>();
    @Override
    public void onTick() {
        super.onTick();
        crystalPlaceableBlocks.clear();
        crystalBlocks.clear();
        for(BlockPos block : BlockUtil.getAll((int) round(placeRange.getValue().doubleValue()))){
            if(CrystalUtil.canPlaceCrystal(block))
                crystalPlaceableBlocks.add(block);
        }

        ArrayList<EntityPlayer> targets = getTargets();

        //Place block
        {
            BlockPos bestPlaceBlock = null;
            float selfDamage = 0;
            float targetDamage = 0;

            for (EntityPlayer player : targets) {
                for (BlockPos blockPos : crystalPlaceableBlocks) {
                    if (bestPlaceBlock == null) {
                        bestPlaceBlock = blockPos;
                        selfDamage = CrystalUtil.calculateDamage(blockPos, mc.player);
                        targetDamage = CrystalUtil.calculateDamage(blockPos, player);
                    } else {
                        if (CrystalUtil.calculateDamage(blockPos, player) > targetDamage && selfDamage >= CrystalUtil.calculateDamage(blockPos, mc.player) && CrystalUtil.calculateDamage(blockPos, mc.player) <= maxSelfDamage.getValue()) {
                            bestPlaceBlock = blockPos;
                            selfDamage = CrystalUtil.calculateDamage(blockPos, mc.player);
                            targetDamage = CrystalUtil.calculateDamage(blockPos, player);
                        }
                    }
                }
            }

            if (bestPlaceBlock != null) {
                placeCrystal(bestPlaceBlock);
                crystalBlocks.add(bestPlaceBlock);
            }
        }

        //Break block
        {
            EntityEnderCrystal bestBreakCrystal = null;
            float selfDamage = 0;
            float targetDamage = 0;

            for(EntityPlayer player : targets) {
                for (EntityEnderCrystal entityEnderCrystal : CrystalUtil.getCrystals(breakRange.getValue())) {
                    if (bestBreakCrystal == null) {
                        bestBreakCrystal = entityEnderCrystal;
                        selfDamage = CrystalUtil.calculateDamage(entityEnderCrystal, mc.player);
                        targetDamage = CrystalUtil.calculateDamage(entityEnderCrystal, player);
                    } else {
                        if (CrystalUtil.calculateDamage(entityEnderCrystal, player) > targetDamage && selfDamage > CrystalUtil.calculateDamage(entityEnderCrystal, mc.player) && CrystalUtil.calculateDamage(entityEnderCrystal, mc.player) <= maxSelfDamage.getValue()) {
                            bestBreakCrystal = entityEnderCrystal;
                            selfDamage = CrystalUtil.calculateDamage(entityEnderCrystal, mc.player);
                            targetDamage = CrystalUtil.calculateDamage(entityEnderCrystal, player);
                        }
                    }
                }
            }

            if(bestBreakCrystal != null)
                breakCrystal(bestBreakCrystal);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                new ArrayList<Entity>(mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            e.setDead();
                        }
                    }
                });
            }
        }
    }

    public void placeCrystal(BlockPos placePos) {
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, hand.getValue(), 0, 0, 0));
        mc.playerController.updateController();
    }

    public void breakCrystal(EntityEnderCrystal entityEnderCrystal){
        this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(entityEnderCrystal));
        this.mc.player.swingArm(hand.getValue());
    }

    public ArrayList<EntityPlayer> getTargets(){
        ArrayList<EntityPlayer> result = new ArrayList<>();
        for(Entity entity : mc.world.loadedEntityList)
            if(entity instanceof EntityPlayer)
                if((EntityPlayer)entity != mc.player)
                    if(BlockUtil.distance(mc.player.getPosition(), entity.getPosition()) <= targetRange.getValue())
                        if(!FriendManager.friends.contains(((EntityPlayer) entity).getDisplayNameString()))
                            result.add((EntityPlayer) entity);

        return result;
    }
}

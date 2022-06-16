package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

import static java.lang.Math.*;

@ModuleArgs(name = "CrystalAura", category = Module.Category.COMBAT)
public class CrystalAura extends Module{
    public Setting<EnumHand> hand = new Setting<>("Hand", EnumHand.MAIN_HAND, this, EnumHand.values());
    public Setting<Double> targetRange = new Setting<>("TargetRange", 10d, this, 0, 15);

    public Setting<Double> placeRange = new Setting<>("PlaceRange", 6d, this, 0, 6);

    public ArrayList<BlockPos> crystalPlaceableBlocks = new ArrayList<>();
    @Override
    public void onTick() {
        super.onTick();
        crystalPlaceableBlocks.clear();
        for(BlockPos block : BlockUtil.getAll((int) round(placeRange.getValue().doubleValue()))){
            if(CrystalUtil.canPlaceCrystal(block))
                crystalPlaceableBlocks.add(block);
        }

        BlockPos bestPlaceBlock = null;

        for(EntityPlayer player : targets()){
            for(BlockPos blockPos : crystalPlaceableBlocks){
                if(bestPlaceBlock == null){
                    bestPlaceBlock = blockPos;
                }else {
                    if(CrystalUtil.calculateDamage(blockPos, player) > CrystalUtil.calculateDamage(bestPlaceBlock, player))
                        bestPlaceBlock = blockPos;
                }
            }
        }
        
        placeCrystal(bestPlaceBlock);
    }

    public void placeCrystal(BlockPos placePos) {
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, hand.getValue(), 0, 0, 0));
        mc.playerController.updateController();
    }

    public ArrayList<EntityPlayer> targets(){
        ArrayList<EntityPlayer> result = new ArrayList<>();
        for(Entity entity : mc.world.loadedEntityList)
            if(entity instanceof EntityPlayer)
                if(BlockUtil.distance(mc.player.getPosition(), entity.getPosition()) <= targetRange.getValue())
                    if(!FriendManager.friends.contains(((EntityPlayer) entity).getDisplayNameString()))
                        result.add((EntityPlayer) entity);

        return result;
    }
}

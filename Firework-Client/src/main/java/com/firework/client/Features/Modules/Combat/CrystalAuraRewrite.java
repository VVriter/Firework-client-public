package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
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
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@ModuleManifest(name = "CrystalAuraRewrite", category = Module.Category.COMBAT)
public class CrystalAuraRewrite extends Module{

    //Hand using what crystals will be placed
    public Setting<InventoryUtil.hands> hand = new Setting<>("Hand", InventoryUtil.hands.MainHand, this, InventoryUtil.hands.values());
    //Range in what we should search for targets
    public Setting<Double> targetRange = new Setting<>("TargetRange", 10d, this, 0, 15);

    //Should display poses of placed crystals
    public Setting<Boolean> displayPlacedCrystals = new Setting<>("DisplayPlacedCrystals", true, this);
    //Resets clear timer delay
    public Setting<Double> resetTimer = new Setting<>("ResetTimerDelayMs", 50d, this, 0, 60).setVisibility(displayPlacedCrystals, true);

    //Ticks delay
    public Setting<Integer> ticksDelay = new Setting<>("TicksDelay", 0, this, 0, 60);

    //Block poses of placed crystals
    public ArrayList<BlockPos> crystalBlocks = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        //Registers listener #1
        Firework.updaterManager.registerUpdater(listener1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        //Unregisters listener #1
        Firework.updaterManager.removeUpdater(listener1);
    }

    //Declares and setups listener #1
    public Updater listener1 = new Updater(){
        @Override
        public void run() {
            super.run();
            this.delay = ticksDelay.getValue();
            doCrystalAura();
        }
    };

    public void doCrystalAura(){

    }

    //Sets exploded crystals dead when sound packet receive
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

    //Renders clicked blocks
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if(displayPlacedCrystals.getValue()) {
            for(BlockPos blockPos : crystalBlocks){
                RenderUtils.blockESP(blockPos);
            }
        }
    }

    //Places crystal at the give blockpos
    public void placeCrystal(BlockPos placePos) {
        //Gets "enumHand" enum from "hands" enum
        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        //Clicks at the given block to place a crystal
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, enumHand, 0, 0, 0));
        mc.playerController.updateController();
    }

    //Breaks given crystal
    public void breakCrystal(EntityEnderCrystal entityEnderCrystal){
        //Gets "enumHand" enum from "hands" enum
        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;

        //Breaks the crystal
        this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(entityEnderCrystal));
        this.mc.player.swingArm(enumHand);
    }

    //Gets targets
    public ArrayList<EntityPlayer> getTargets(){
        ArrayList<EntityPlayer> result = new ArrayList<>();
        for(Entity entity : mc.world.loadedEntityList)
            if(entity instanceof EntityPlayer)
                if((EntityPlayer)entity != mc.player)
                    if(mc.player.getPositionVector().distanceTo(entity.getPositionVector()) <= targetRange.getValue())
                        if(!FriendManager.friends.contains(((EntityPlayer) entity).getDisplayNameString()))
                            result.add((EntityPlayer) entity);

        return result;
    }
}

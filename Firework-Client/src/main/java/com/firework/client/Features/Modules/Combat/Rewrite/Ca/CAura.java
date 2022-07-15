package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@ModuleManifest(name = "CAura",category = Module.Category.COMBAT)
public class CAura extends Module {

    public Setting<Integer> range = new Setting<>("Range", 5, this, 1, 10);
    public Setting<Boolean> legal = new Setting<>("Legal", true, this);

    public Setting<Integer> placeDelay = new Setting<>("PlaceDelay", 200, this, 1, 1000);
    public Setting<Integer> breakDelay = new Setting<>("BreakDelay", 200, this, 1, 1000);
    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 5, this, 1, 20);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 5, this, 1, 20);

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this, ItemUser.switchModes.values());
    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);

    Timer delayTimer;

    EntityPlayer target;

    ItemUser user;

    @Override
    public void onEnable() {
        super.onEnable();
        delayTimer = new Timer();
        user = new ItemUser(this,switchMode,rotate);
        canBreak = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTimer = null;
        user = null;
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = (SPacketSoundEffect) event.getPacket();
            if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                final SPacketSoundEffect sPacketSoundEffect = new SPacketSoundEffect();
                new ArrayList(this.mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal && ((EntityEnderCrystal) e).getDistanceSq(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) < 36.0) {
                        ((EntityEnderCrystal) e).setDead();
                    }
                });
            }
        }
    }
    BlockPos posToPlace = null;
    EntityEnderCrystal entityEnderCrystal = null;
    boolean canBreak;
    @Override
    public void onTick() {
        super.onTick();
        target = PlayerUtil.getClosestTarget(range.getValue());

        if(target == null) return;

        if(delayTimer.hasPassedMs(breakDelay.getValue()) && canBreak) {
            entityEnderCrystal = CrystalUtils.getBestCrystal(target, range.getValue());

            canBreak = false;
            if (entityEnderCrystal != null) {
                breakCrystal(entityEnderCrystal);
                entityEnderCrystal = null;
                return;
            }
        }

        if(delayTimer.hasPassedMs(placeDelay.getValue() + breakDelay.getValue())) {
            posToPlace = CrystalUtils.bestCrystalPos(target,range.getValue(),legal.getValue(), maxSelfDmg.getValue(), minTargetDmg.getValue());

            if (posToPlace != null) {
                //AutoCrystal code
                placeCrystal(posToPlace);
                posToPlace = null;
                canBreak = true;
                delayTimer.reset();
                return;
            }
        }
    }

    public void placeCrystal(BlockPos pos){
        user.useItem(Items.END_CRYSTAL, pos, EnumHand.MAIN_HAND);
    }

    public void breakCrystal(EntityEnderCrystal enderCrystal){
        Firework.rotationManager.rotateSpoof(enderCrystal.getPositionVector());
        mc.playerController.attackEntity(mc.player, enderCrystal);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if(target == null) return;
        if (posToPlace != null) {
            RenderUtils.drawProperBox(target.getPosition(),new Color(203, 3, 3,200));
            RenderUtils.drawBoxESP(posToPlace,Color.BLUE,1,true,true,200,1);
        }
        if(entityEnderCrystal != null){
            RenderUtils.drawBoundingBox(entityEnderCrystal.getRenderBoundingBox(), Color.GREEN, 3);
        }
    }

    @SubscribeEvent
    public void onSettingChangeEvent(SettingChangeValueEvent event){
        if(event.setting == placeDelay || event.setting == breakDelay) {
            delayTimer.reset();
            entityEnderCrystal = null;
            posToPlace = null;
        }
    }
}

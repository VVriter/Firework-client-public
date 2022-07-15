package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ICPacketUseEntity;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@ModuleManifest(name = "MyCAura",category = Module.Category.COMBAT)
public class MyCaura extends Module {

    public Setting<Integer> range = new Setting<>("Range", 5, this, 1, 10);
    public Setting<Boolean> legal = new Setting<>("Legal", true, this);

    public Setting<Integer> placeDelay = new Setting<>("PaceDelay", 200, this, 1, 1000);
    public Setting<Integer> breakDelay = new Setting<>("BreakDelay", 200, this, 1, 1000);
    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 5, this, 1, 20);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 5, this, 1, 20);

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this, ItemUser.switchModes.values());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Rotate", true, this);

    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();

    EntityPlayer target;
    EntityEnderCrystal crystalsionio;

    ItemUser user;

    @Override public void onEnable() {super.onEnable();
        placeTimer.reset();
        breakTimer.reset();
        user = new ItemUser(this,switchMode,rotate);
    }

    @Override public void onDisable() {super.onDisable();
        placeTimer.reset();
        breakTimer.reset();
        user = null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        target = PlayerUtil.getClosestTarget(range.getValue());
        crystalsionio = PlayerUtil.getClosestCrystal(range.getValue());
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51 && this.posesCrystalsPlaced.contains(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ()))) {
                final ICPacketUseEntity use = (ICPacketUseEntity)new CPacketUseEntity();
                use.setEntityId(packet.getEntityID());
                use.setAction(CPacketUseEntity.Action.ATTACK);
                mc.getConnection().sendPacket((Packet)use);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
        }
    }

    BlockPos posToPlace;
    ArrayList<BlockPos> posesCrystalsPlaced = new ArrayList<>();
    @Override public void onTick() {super.onTick();
        posToPlace = CrystalUtils.bestCrystalPos(target,range.getValue(),legal.getValue());

        if (target != null && posToPlace != null) {
            //AutoCrystal code
            if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                user.useItem(Items.END_CRYSTAL,posToPlace, EnumHand.MAIN_HAND);
                posesCrystalsPlaced.add(posToPlace);
                placeTimer.reset();
            }

          /*  if (breakTimer.hasPassedMs(breakDelay.getValue())) {
                EntityUtil.attackEntity(crystalsionio,packet.getValue(),true);
                breakTimer.reset();
            } */

        }
    }

    @SubscribeEvent public void onRender(RenderWorldLastEvent e) {
        if (target != null && posToPlace != null) {
          //  RenderUtils.drawProperBox(target.getPosition(),new Color(203, 3, 3,200));
            RenderUtils.drawBoxESP(posToPlace,Color.BLUE,1,true,true,200,1);
        }
    }
}

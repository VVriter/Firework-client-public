package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@ModuleManifest(name = "CAura",category = Module.Category.COMBAT)
public class CAura extends Module {

    public Setting<Integer> range = new Setting<>("Range", 5, this, 1, 10);
    public Setting<Boolean> legal = new Setting<>("Legal", true, this);

    public Setting<Integer> delayValue = new Setting<>("Delay", 200, this, 1, 1000);
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
    }

    @Override
    public void onDisable() {
        super.onDisable();
        delayTimer = null;
        user = null;
    }

    ArrayList<Packet> packets = new ArrayList<>();

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51 && this.posesCrystalsPlaced.contains(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ()))) {
                final ICPacketUseEntity use = (ICPacketUseEntity)new CPacketUseEntity();
                use.setEntityId(packet.getEntityID());
                use.setAction(CPacketUseEntity.Action.ATTACK);
                Firework.rotationManager.rotateSpoof(new Vec3d(packet.getX() + 0.5, packet.getY() + 0.5, packet.getZ() + 0.5));
                mc.getConnection().sendPacket((Packet<?>) use);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                packets.add((Packet) use);
            }
        }else if (event.getPacket() instanceof SPacketSoundEffect) {
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
    BlockPos posToPlace;
    ArrayList<BlockPos> posesCrystalsPlaced = new ArrayList<>();
    @Override
    public void onTick() {
        super.onTick();
        target = PlayerUtil.getClosestTarget(range.getValue());

        posToPlace = CrystalUtils.bestCrystalPos(target,range.getValue(),legal.getValue(), maxSelfDmg.getValue(), minTargetDmg.getValue());

        if (target != null && posToPlace != null) {
            //AutoCrystal code
            user.useItem(Items.END_CRYSTAL,posToPlace, EnumHand.MAIN_HAND, packet.getValue());
            posesCrystalsPlaced.add(posToPlace);
        }

    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (target != null && posToPlace != null) {
            RenderUtils.drawProperBox(target.getPosition(),new Color(203, 3, 3,200));
            RenderUtils.drawBoxESP(posToPlace,Color.BLUE,1,true,true,200,1);
        }
    }
}

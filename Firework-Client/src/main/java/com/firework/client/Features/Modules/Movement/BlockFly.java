package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.World.Scaffold.ScaffoldBlock;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Movement.PlayerPushOutOfBlocksEvent;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Mixins.MixinsList.ISPacketPlayerPosLook;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "BlockFlyRewrite", category = Module.Category.MOVEMENT)
public class BlockFly extends Module {
    private Setting<Boolean> packet  = new Setting<>("Packet", false, this);

    private Setting<Boolean> rotate  = new Setting<>("Rotate", true, this);

    private Setting<BlockPlacer.switchModes> switchMode  = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this);

    private Setting<Boolean> Tower  = new Setting<>("Tower", true, this);

    private Setting<Boolean> slowness  = new Setting<>("Slowness", true, this);
    private Setting<Double> speed = new Setting<>("Speed", 0d, this, 0, 1).setVisibility(v-> slowness.getValue(true));

    private Setting<Double> towerTimerDelay = new Setting<>("TowerDelayMs", 1.4d, this, 0, 4);
    private Setting<Double> rollBackDelay = new Setting<>("RollBackDelayS", 2.1d, this, 0, 3);

    private Setting<Boolean> velocity  = new Setting<>("Velocity", true, this);

    private Setting<Boolean> resetOnPacketLookPos  = new Setting<>("ResetOnPacketLookPos", false, this);
    private Setting<Boolean> confirmTeleport  = new Setting<>("ConfirmTeleport", false, this);

    private Setting<Boolean> noForceRotate  = new Setting<>("NoForceRotate", true, this);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();

    private BlockPos pos;

    private BlockPlacer blockPlacer;

    private Timer motionTimer, towerTimer, rollBackTimer;

    public BlockFly(){
        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        towerTimer = new Timer();
        towerTimer.reset();
        rollBackTimer = new Timer();
        rollBackTimer.reset();
        motionTimer = new Timer();
        motionTimer.reset();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (fullNullCheck()) return;

        if(slowness.getValue()) {
            final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }

        if(Tower.getValue()){
            if(mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F
                    && towerTimer.hasPassedMs(towerTimerDelay.getValue()*100)){
                if (rollBackTimer.hasPassedMs(rollBackDelay.getValue()*1000)) {
                    rollBackTimer.reset();
                    mc.player.motionY = -0.28f;
                }else {
                    final float towerMotion = 0.41999998688f;
                    mc.player.setVelocity(0, towerMotion, 0);
                    Firework.positionManager.setPositionPacket(mc.player.posX, mc.player.posY + towerMotion, mc.player.posZ, true, false, false);
                }
                towerTimer.reset();
               // mc.player.setVelocity(0, 0.42, 0);
                //Firework.positionManager.setPositionPacket(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, onGround.getValue(), setPos.getValue(), noLagBack.getValue());
                //flyTimer.reset();
            }
        }

        pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (isAir(pos)) {
            blockPlacer.placeBlock(pos, Block.getBlockFromItem(InventoryUtil.getItemStack(InventoryUtil.findAnyBlock()).getItem()));
            blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
        }
    });

    @Subscribe
    public Listener<PlayerPushOutOfBlocksEvent> onPush = new Listener<>(e -> {
        e.setCancelled(velocity.getValue());
    });

    @Subscribe
    public Listener<SettingChangeValueEvent> listener3 = new Listener<>(event-> {
        if(event.setting == towerTimerDelay || event.setting == rollBackDelay){
            towerTimer.reset();
            rollBackTimer.reset();
        }
    });

    @Subscribe
    public Listener<PacketEvent.Receive> onRender = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (resetOnPacketLookPos.getValue())
                towerTimer.reset();
            if (confirmTeleport.getValue()) {
                mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());

                mc.getConnection().sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
                mc.getConnection().sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            }
            if(noForceRotate.getValue()){
                ((ISPacketPlayerPosLook) packet).setYaw(mc.player.rotationYaw);
                ((ISPacketPlayerPosLook) packet).setPitch(mc.player.rotationPitch);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
        }
    });

    @Override
    public void onDisable() {
        super.onDisable();
        blocksToRender.clear();
    }

    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}

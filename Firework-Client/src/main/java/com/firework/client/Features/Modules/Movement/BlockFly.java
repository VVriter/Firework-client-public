package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.World.Scaffold.ScaffoldBlock;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.ICPacketPlayer;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.ISPacketPlayerPosLook;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "BlockFly", category = Module.Category.MOVEMENT)
public class BlockFly extends Module {

    private Setting<Boolean> packet  = new Setting<>("Packet", false, this);

    private Setting<Boolean> rotate  = new Setting<>("Rotate", true, this);

    private Setting<Boolean> swing  = new Setting<>("Swing", true, this);
    private Setting<Boolean> forceGroundPacket = new Setting<>("ForceGround", true, this);

    private Setting<BlockPlacer.switchModes> switchMode  = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this, BlockPlacer.switchModes.values());

    private Setting<Boolean> Tower  = new Setting<>("Tower", true, this);

    private Setting<Boolean> slowness  = new Setting<>("Slowness", true, this);
    private Setting<Double> speed = new Setting<>("Delay", 0d, this, 0, 1).setVisibility(slowness, true);

    private Setting<Integer> flyTimerDelay = new Setting<>("FlyDelayMs", 116, this, 0, 400);

    private Setting<Boolean> onGround  = new Setting<>("OnGround", true, this);
    private Setting<Boolean> setPos  = new Setting<>("SetPos", false, this);
    private Setting<Boolean> noLagBack  = new Setting<>("NoLagBag", false, this).setVisibility(setPos, true);

    private Setting<Boolean> velocity  = new Setting<>("Velocity", true, this);

    private Setting<Boolean> resetOnPacketLookPos  = new Setting<>("ResetOnPacketLookPos", true, this);
    private Setting<Boolean> confirmTeleport  = new Setting<>("ConfirmTeleport", true, this);

    private Setting<Boolean> noForceRotate  = new Setting<>("NoForceRotate", true, this);
    private Setting<Boolean> jumpIfColliededHoriz  = new Setting<>("JumpOnCollide", false, this);
    private Setting<Boolean> rotationSpoof  = new Setting<>("RotationSpoof", true, this);
    private Setting<Boolean> noLagBackPacket  = new Setting<>("NoLagBackPacket", true, this);
    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();

    private BlockPos pos;

    private BlockPlacer blockPlacer;

    private Timer flyTimer;

    public BlockFly(){
        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        flyTimer = new Timer();
        flyTimer.reset();
    }

    @Override
    public void onTick() {
        super.onTick();
        if (mc.player == null || mc.world == null) return;

        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }

        if(slowness.getValue()) {
            double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            mc.player.motionX = calc[0];
            mc.player.motionZ = calc[1];
        }

        if(Tower.getValue()){
            if(mc.gameSettings.keyBindJump.isKeyDown() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F){
                if(flyTimer.hasPassedMs(flyTimerDelay.getValue())) {
                    mc.player.setVelocity(0, 0.42, 0);
                    Firework.positionManager.setPositionPacket(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, onGround.getValue(), setPos.getValue(), noLagBack.getValue());
                    flyTimer.reset();
                }
                pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
                if (isAir(pos)) {
                    blockPlacer.placeBlock(pos, Block.getBlockFromItem(InventoryUtil.getItemStack(InventoryUtil.findAnyBlock()).getItem()));
                    blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPush(PlayerSPPushOutOfBlocksEvent e){
        e.setCanceled(velocity.getValue());
        if(mc.player.collidedHorizontally && jumpIfColliededHoriz.getValue())
            mc.player.jump();
    }

    @SubscribeEvent
    public void onSettingUpdate(SettingChangeValueEvent event){
        if(event.setting == flyTimerDelay){
            flyTimer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && forceGroundPacket.getValue()) {
            ((ICPacketPlayer) event.getPacket()).setOnGround(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && rotationSpoof.getValue()) {
            ((ICPacketPlayer)event.getPacket()).setPitch(90);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        if(event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (resetOnPacketLookPos.getValue())
                flyTimer.reset();
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
            Block block = BlockUtil.getBlock(EntityUtil.getFlooredPos(packet.getX(), packet.getY()+1, packet.getZ()));
            if(noLagBackPacket.getValue() && block != Blocks.AIR){
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blocksToRender.clear();
    }

    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }
}

package com.firework.client.Features.Modules.Movement.blockFly;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.World.Scaffold.ScaffoldBlock;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Mixins.MixinsList.Interfaces.ICPacketPlayer;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "BlockFlyRewriteV2", category = Module.Category.MOVEMENT)
public class BlockFlyRewriteV2 extends Module {

    private Setting<Boolean> packet  = new Setting<>("Packet", true, this);

    private Setting<Boolean> rotate  = new Setting<>("Rotate", true, this);

    private Setting<Boolean> swing  = new Setting<>("Swing", true, this);
    private Setting<Boolean> forceGroundPacket = new Setting<>("ForceGround", true, this);

    private Setting<BlockPlacer.switchModes> switchMode  = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this, BlockPlacer.switchModes.values());

    private Setting<Boolean> Tower  = new Setting<>("Tower", true, this);

    private Setting<Boolean> slowness  = new Setting<>("Slowness", true, this);
    private Setting<Double> speed = new Setting<>("Delay", 0.7, this, 0, 1).setVisibility(slowness, true);

    private Setting<Integer> flyTimerDelay = new Setting<>("FlyDelayMs", 220, this, 0, 400);

    private Setting<Boolean> onGround  = new Setting<>("OnGround", true, this);
    private Setting<Boolean> setPos  = new Setting<>("SetPos", true, this);
    private Setting<Boolean> noLagBack  = new Setting<>("NoLagBag", true, this);

    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();

    private BlockPos pos;

    private BlockPlacer blockPlacer;

    private Timer flyTimer;

    public BlockFlyRewriteV2(){
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

        pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (isAir(pos)) {
            blockPlacer.placeBlock(pos, Block.getBlockFromItem(InventoryUtil.getItemStack(InventoryUtil.findAnyBlock()).getItem()));
            blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
        }

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
                    Firework.positionManager.setPositionPacket(mc.player.posX, mc.player.posY -0.2444441D, mc.player.posZ, onGround.getValue(), setPos.getValue(), noLagBack.getValue());
                    mc.player.motionY = -0.2444441D;
                    mc.player.motionZ = 0.0D;
                    mc.player.motionX = 0.0D;

                    mc.player.setVelocity(0, 0.42, 0);
                    Firework.positionManager.setPositionPacket(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, onGround.getValue(), setPos.getValue(), noLagBack.getValue());
                    flyTimer.reset();
                }
            }
        }
    }

    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
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
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blocksToRender.clear();
    }
}

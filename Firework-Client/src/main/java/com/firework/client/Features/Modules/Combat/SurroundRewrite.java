package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "SurroundRewrite", category = Module.Category.COMBAT)
public class SurroundRewrite extends Module{
    public Setting<Boolean> shouldDisableOnJump = new Setting<>("DisableOnJump", true, this);
    public Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);

    public Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", true, this);
    public Setting<Integer> tickDelay = new Setting<>("TickDelay", 0, this, 0, 20).setVisibility(shouldToggle, false);
    public Setting<Double> placeDelay = new Setting<>("PlaceDelayS", 0d, this, 0, 20).setVisibility(shouldToggle, false);

    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);

    public Setting<Boolean> useEnderChestIfNoObsFound = new Setting<>("UseEChestAsReserv", true, this);

    public Setting<InventoryUtil.hands> hand = new Setting<>("hand", InventoryUtil.hands.MainHand, this, InventoryUtil.hands.values());

    Timer placeTimer;

    private int lastKeyCode;

    private ArrayList<BlockPos> line;

    public SurroundRewrite(){
        line = new ArrayList<>();
        lastKeyCode = -1;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        placeTimer = new Timer();
        placeTimer.reset();

        if(shouldCenter.getValue())
            center();
    }

    @Override
    public void onTick() {
        super.onTick();
        this.delay = tickDelay.getValue();

        if(!mc.player.inventory.hasItemStack(new ItemStack(Blocks.OBSIDIAN))
                | (useEnderChestIfNoObsFound.getValue() ? (mc.player.inventory.hasItemStack(new ItemStack(Blocks.ENDER_CHEST)) ? false : true) : true))
            return;

        doSurround(getBlocksToPlace());
    }

    private void doSurround(BlockPos[] blocksToPlace){
        EnumHand enumHand = hand.getValue(InventoryUtil.hands.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;


        if(lastKeyCode == mc.gameSettings.keyBindJump.getKeyCode()) {
            BlockUtil.placeBlock(EntityUtil.getFlooredPos(mc.player).add(0, -1, 0), enumHand, rotate.getValue(), packet.getValue(), true);
            lastKeyCode = -1;
        }

        if(placeDelay.getValue(0d)) {
            for (int i = 0; i < line.size(); i++) {
                if (BlockUtil.getBlock(line.get(i)) != Blocks.AIR)
                    line.remove(i);
            }

            for (BlockPos blockPos : getBlocksToPlace()) {
                if (BlockUtil.getBlock(blockPos) == Blocks.AIR) {
                    line.add(blockPos);
                }
            }

            for (BlockPos blockPos : line){
                if(placeTimer.passedS(placeDelay.getValue())){
                    BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), true);
                    placeTimer.reset();
                }
            }
        }else{
            for (BlockPos blockPos : getBlocksToPlace()){
                BlockUtil.placeBlock(blockPos, enumHand, rotate.getValue(), packet.getValue(), true);
            }
        }
    }

    @SubscribeEvent
    private void onPlayerJump(LivingEvent.LivingJumpEvent e){
        if(e.getEntity() instanceof EntityPlayer){
            if(shouldDisableOnJump.getValue())
                onDisable();
            else
                doSurround(getBlocksToPlace(new BlockPos(0, 1, 0)));
        }
    }

    @SubscribeEvent
    private void onPressedKey(InputEvent.KeyInputEvent event) {
        lastKeyCode = Keyboard.getEventKey();
    }

    private void center() {
        if (isCentered()) {
            return;
        }

        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
    }

    private boolean isCentered() {
        double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
        return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
    }

    private BlockPos[] getBlocksToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, -1, 0), p.add(-1, -1, 0), p.add(0, -1, 1), p.add(0, -1, -1), p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }

    private BlockPos[] getBlocksToPlace(BlockPos offset) {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, -1, 0).add(offset), p.add(-1, -1, 0).add(offset), p.add(0, -1, 1).add(offset), p.add(0, -1, -1).add(offset), p.add(1, 0, 0).add(offset), p.add(-1, 0, 0).add(offset), p.add(0, 0, 1).add(offset), p.add(0, 0, -1).add(offset)};
    }
}

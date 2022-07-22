package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Features.Modules.Movement.Step;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.TestEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Blocks.HoleUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.MotionUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Listener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.firework.client.Implementations.Utill.InventoryUtil.getHotbarItemSlot;

@ModuleManifest(name = "Surround", category = Module.Category.COMBAT)
public class Surround extends Module {
    private Setting<Boolean> shouldDisableOnJump = new Setting<>("DisableOnJump", true, this);

    private Setting<Boolean> shouldCenter = new Setting<>("Center", true, this);
    private Setting<MotionUtil.centerModes> centerMode = new Setting<>("CMode", MotionUtil.centerModes.Motion, this, MotionUtil.centerModes.values()).setVisibility(v-> shouldCenter.getValue(true));

    private Setting<Boolean> shouldToggle = new Setting<>("ShouldToggle", false, this);
    private Setting<Integer> placeDelay = new Setting<>("PlaceDelayMs", 0, this, 0, 100);

    private Setting<BlockPlacer.switchModes> switchMode = new Setting<>("Switch", BlockPlacer.switchModes.Silent, this, BlockPlacer.switchModes.values());

    private Setting<Boolean> rotate = new Setting<>("Rotate", false, this);
    private Setting<Boolean> packet = new Setting<>("Packet", true, this);

    private ArrayList<BlockPos> line;

    private BlockPlacer blockPlacer;

    private Timer placeTimer;

    private boolean first;

    public static long time = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        if(fullNullCheck()) return;

        if(!containsAir(blockToPlace()) && shouldToggle.getValue())
            onDisable();

        placeTimer = new Timer();
        placeTimer.reset();

        blockPlacer = new BlockPlacer(this, switchMode, rotate, packet);

        line = new ArrayList<>();

        first = true;

        Firework.eventBus.register(listener1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        line = null;
        blockPlacer = null;
        placeTimer = null;
        Firework.eventBus.unregister(listener1);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange packet2 = ((SPacketBlockChange) event.getPacket());
            if(packet2.blockState.getBlock() == Blocks.AIR){
                if(Arrays.asList(blockToPlace()).contains(packet2.getBlockPosition())){
                    if(InventoryUtil.getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1){
                        blockPlacer.placeBlock(packet2.getBlockPosition(), Blocks.OBSIDIAN);
                        System.out.println(1);
                    }
                }
            }
        }
    }

    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        System.out.println(System.nanoTime() - time);
        if (shouldDisableOnJump.getValue() && mc.gameSettings.keyBindJump.isKeyDown()) {
            onDisable();
        }

        if (mc.player.collidedHorizontally && shouldDisableOnJump.getValue() && Step.enabled.getValue()) {
            onDisable();
        }

        if (mc.player == null || mc.world == null) return;

        if (first) {
            if (shouldCenter.getValue())
                MotionUtil.autoCenter(centerMode);

            if (shouldToggle.getValue()) {
                doSurround();
                onDisable();
            }
            first = false;
        }

        //Stops process if didn't find obby in a hotbar
        if (getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            MessageUtil.sendError("No obby found in the hotbar", -1117);
            return;
        }

        final BlockPos[] blockToPlace = blockToPlace();

        if (containsAir(blockToPlace))
            doSurround(blockToPlace);
    });

    private void doSurround(BlockPos... blockToPlace){
        try {
            for (BlockPos pos : blockToPlace)
                if (isAir(pos) && !line.contains(pos))
                    line.add(pos);

            ArrayList<BlockPos> placedBlocks = new ArrayList<>();

            for (BlockPos pos : line) {
                if (placeTimer.hasPassedMs(placeDelay.getValue())) {
                    if (BlockUtil.getPossibleSides(pos).isEmpty() && isValid(pos.add(0, -1, 0)))
                        blockPlacer.placeBlock(pos.add(0, -1, 0), Blocks.OBSIDIAN);
                    else {
                        if(!isValid(pos)) return;
                        blockPlacer.placeBlock(pos, Blocks.OBSIDIAN);
                        placedBlocks.add(pos);
                    }
                    placeTimer.reset();
                } else {
                    break;
                }
            }

            line.removeAll(placedBlocks);
        }catch (Exception e){}
    }
    //Returns blocks to place
    public BlockPos[] blockToPlace() {
        BlockPos p = EntityUtil.getFlooredPos(mc.player);
        return new BlockPos[]{p.add(1, 0, 0), p.add(-1, 0, 0), p.add(0, 0, 1), p.add(0, 0, -1)};
    }

    //Checks if a block pos array contains any air blocks
    public boolean containsAir(BlockPos... blocks){
        for(BlockPos blockPos : blocks){
            if(BlockUtil.getBlock(blockPos) == Blocks.AIR){
                return true;
            }
        }
        return false;
    }

    //Returns true if there is no block at a give pos
    private boolean isAir(BlockPos pos) {
        return BlockUtil.getBlock(pos) == Blocks.AIR;
    }

    //Checks if block is valid
    public static boolean isValid(BlockPos pos){
        if (!mc.world.checkNoEntityCollision(new AxisAlignedBB(pos)))
            return false;
        return true;
    }
}

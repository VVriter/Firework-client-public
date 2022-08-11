package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Player.BlockClickEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.RotationUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.LinkedList;

@ModuleManifest(
        name = "PacketMine",
        category = Module.Category.WORLD,
        description = "breakBlocksFaster!"
)
public class PacketMine extends Module {

    private float curBlockDamage;
    private int originalSlot;
    private BlockInfo currentBlock;
    private Timer timer = new Timer();

    private Runnable postAction;

    private final LinkedList<BlockInfo> blocks = new LinkedList<>();


    @Override
    public void onEnable() {
        super.onEnable();
        blocks.clear();
        currentBlock = null;
        curBlockDamage = 0F;
        postAction = null;
        originalSlot = -1;
    }

    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        LinkedList<BlockInfo> renderBlocks = (LinkedList<BlockInfo>) blocks.clone();
        if (currentBlock != null) {
            renderBlocks.add(currentBlock);
        }
        if (renderBlocks != null) {
            for (BlockInfo pos : renderBlocks) {
                RenderUtils.drawBoxESP(pos.getPos(), Color.white,1,true,true,160,1);
            }
        }
    });

    public Setting<Boolean> queue = new Setting<>("queue", true, this);
    public Setting<Boolean> swap = new Setting<>("swap", true, this);
    public Setting<Double> reach = new Setting<>("reach", (double)3, this, 1, 10);

    public Setting<Boolean> rotate = new Setting<>("rotate", true, this);

    public Setting<Double> delay = new Setting<>("delay", (double)3, this, 1, 5);


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> event = new Listener<>(e-> {
        if (currentBlock != null) {
            if (mc.world.getBlockState(currentBlock.getPos()).getBlock().equals(currentBlock.getStartState().getBlock()) &&
                    mc.player.getPositionEyes(1F).distanceTo(new Vec3d(currentBlock.getPos()).add(new Vec3d(currentBlock.getFacing().getDirectionVec()).scale(0.5D))) <= reach.getValue()) {
                updateProgress();
                if (swap.getValue() && curBlockDamage >= 1F && originalSlot == -1) {
                    int bestSlot = AutoTool.bestIntSlot(currentBlock.getPos());
                    if (bestSlot != -1 && bestSlot != mc.player.inventory.currentItem) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(bestSlot));
                        originalSlot = bestSlot;
                    }
                }
            } else {
                currentBlock = null;
                if (originalSlot != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(originalSlot));
                    originalSlot = -1;
                }
            }

        } else if (!blocks.isEmpty()) {

            BlockInfo info = blocks.peek();

            if (mc.world.getBlockState(info.getPos()).getBlock().equals(info.getStartState().getBlock()) &&
                    mc.player.getPositionEyes(1F).distanceTo(new Vec3d(info.getPos()).add(new Vec3d(info.getFacing().getDirectionVec()).scale(0.5D))) <= reach.getValue()) {
                blocks.poll();
                currentBlock = info;
                curBlockDamage = 0F;
                timer.reset();
                postAction = () -> {
                    if(currentBlock != null) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, currentBlock.getPos(), currentBlock.getFacing()));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentBlock.getPos(), currentBlock.getFacing()));
                    }
                };
                updateProgress();
            } else {
                blocks.poll();
            }

        }

        if (rotate.getValue() && currentBlock != null) {
            float rotations[] = Firework.rotationManager.getRotations(new Vec3d(currentBlock.getPos()).add(new Vec3d(currentBlock.getFacing().getDirectionVec()).scale(0.5D)));
            RotationUtil.packetFacePitchAndYaw(rotations[1],rotations[0]);
            e.setCancelled(true);
        }
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> evv = new Listener<>(e-> {
        if (postAction != null && timer.hasPassedMs(delay.getValue() * 50F)) {
            postAction.run();
            postAction = null;
            timer.reset();
        }
    });

    @Subscribe
    public Listener<BlockClickEvent> ev = new Listener<>(e-> {
        e.setCancelled(true);
        if (e.getPos() == null || !canBreak(e.getPos())) return;

        BlockInfo info = new BlockInfo(e.getPos(), e.getFacing());

        if (!blocks.contains(info) && (queue.getValue() || (blocks.isEmpty() && currentBlock == null))) {
            blocks.add(info);
        }
    });




    private void updateProgress() {

        if (curBlockDamage >= 1F) {
            return;
        }

        IBlockState state = currentBlock.getStartState();
        int bestSlot = AutoTool.bestIntSlot(currentBlock.getPos());

        if (bestSlot == -1) {
            bestSlot = mc.player.inventory.currentItem;
        }

        int prevItem = mc.player.inventory.currentItem;

        mc.player.inventory.currentItem = bestSlot;
        curBlockDamage += state.getPlayerRelativeBlockHardness(mc.player, mc.world, currentBlock.getPos());
        mc.player.inventory.currentItem = prevItem;
    }


    private boolean canBreak(BlockPos pos) {
        try {
            final IBlockState blockState = mc.world.getBlockState(pos);
            return blockState.getBlockHardness(mc.world, pos) != -1;
        } catch (NullPointerException e) {
            return false;
        }
    }



    private static class BlockInfo {

        protected final BlockPos pos;
        protected final EnumFacing facing;
        protected final IBlockState startState;

        public BlockInfo(BlockPos pos, EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
            this.startState = mc.world.getBlockState(pos);
        }

        public BlockPos getPos() {
            return pos;
        }

        public EnumFacing getFacing() {
            return facing;
        }

        public IBlockState getStartState() {
            return startState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BlockInfo blockInfo = (BlockInfo) o;
            return pos.equals(blockInfo.pos);
        }

    }
}

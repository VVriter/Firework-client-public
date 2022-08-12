package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.BlockClickEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;
import com.firework.client.Implementations.Mixins.MixinsList.IPlayerControllerMP;

import java.awt.*;

@ModuleManifest(
        name = "InstantMine",
        category = Module.Category.WORLD,
        description = "Instantly mines blocks"
)
public class InstantMine extends Module {

    public Setting<Boolean> autoBreak = new Setting<>("AutoBreak", false, this);
    public Setting<Boolean> picOnly = new Setting<>("PickaxeOnly", false, this);
    public Setting<Integer> delay = new Setting<>("Delay(ms)", 200, this, 1, 500);

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(180, 50, 50), this);

    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel = false;
    private Timer breaktimer = new Timer();
    private EnumFacing direction;

    @Override
    public void onEnable() {
        super.onEnable();
        packetCancel = false;
        breaktimer = new Timer();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        breaktimer = null;
    }

    @Subscribe
    public Listener<Render3dE> onRender = new Listener<>(e-> {
        if (renderBlock != null)
            RenderUtils.drawBoxESP(renderBlock, color.getValue().toRGB(),3,true,true,100,1);
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        if(renderBlock != null) {
            if(autoBreak.getValue() && breaktimer.hasPassedMs(delay.getValue())) {
                if(picOnly.getValue()&&!(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_PICKAXE)) return;
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        renderBlock, direction));
                breaktimer.reset();
            }

        }

        ((IPlayerControllerMP)mc.playerController).setBlockHitDelay(0);
    });

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(e-> {
        Packet packet = e.getPacket();
        if (packet instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging digPacket = (CPacketPlayerDigging) packet;
            if(digPacket.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && packetCancel) e.setCancelled(true);
        }
    });

    @Subscribe
    public Listener<BlockClickEvent> blockClickEvent = new Listener<>(e-> {
        if (canBreak(e.getPos())) {

            if(lastBlock==null||e.getPos().getX()!=lastBlock.getX() || e.getPos().getY()!=lastBlock.getY() || e.getPos().getZ()!=lastBlock.getZ()) {
                packetCancel = false;
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        e.getPos(), e.getFacing()));
                packetCancel = true;
            }else
                packetCancel = true;
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    e.getPos(), e.getFacing()));

            renderBlock = e.getPos();
            lastBlock = e.getPos();
            direction = e.getFacing();

            e.setCancelled(true);
        }
    });

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    public BlockPos getTarget(){
        return renderBlock;
    }

    public void setTarget(BlockPos pos){
        renderBlock = pos;
        packetCancel = false;
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                pos, EnumFacing.DOWN));
        packetCancel = true;
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                pos, EnumFacing.DOWN));
        direction = EnumFacing.DOWN;
        lastBlock = pos;
    }
}

package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.Player.BlockClickEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
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

    public Setting<Boolean> autoBreak = new Setting<>("autoBreak", false, this);
    public Setting<Boolean> picOnly = new Setting<>("autoBreak", false, this);

    public Setting<Double> delay = new Setting<>("tD", (double)3, this, 1, 10);
    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel = false;
    private Timer breaktimer = new Timer();
    private EnumFacing direction;


    @Subscribe
    public Listener<Render3dE> listener1 = new Listener<>(e-> {
        if (renderBlock != null) {
            RenderUtils.drawBoxESP(renderBlock, Color.RED,1,true,true,100,1);
        }
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

        try {
            ((IPlayerControllerMP)mc.playerController).setBlockHitDelay(0);
        } catch (Exception ex) {
        }
    });


    @Subscribe
    public Listener<PacketEvent.Send> evv = new Listener<>(e-> {
        Packet packet = e.getPacket();
        if (packet instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging digPacket = (CPacketPlayerDigging) packet;
            if(((CPacketPlayerDigging) packet).getAction()== CPacketPlayerDigging.Action.START_DESTROY_BLOCK && packetCancel) e.setCancelled(true);
        }
    });

    @Subscribe
    public Listener<BlockClickEvent> evvvv = new Listener<>(e-> {
        if (canBreak(e.getPos())) {

            if(lastBlock==null||e.getPos().getX()!=lastBlock.getX() || e.getPos().getY()!=lastBlock.getY() || e.getPos().getZ()!=lastBlock.getZ()) {
                //Command.sendChatMessage("New Block");
                packetCancel = false;
                //Command.sendChatMessage(p_Event.getPos()+" : "+lastBlock);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        e.getPos(), e.getFacing()));
                packetCancel = true;
            }else{
                packetCancel = true;
            }
            //Command.sendChatMessage("Breaking");
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

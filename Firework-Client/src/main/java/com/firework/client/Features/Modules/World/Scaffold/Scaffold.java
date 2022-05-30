package com.firework.client.Features.Modules.World.Scaffold;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.BlockPos;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Scaffold extends Module {

    public Setting<Boolean> rotate  = new Setting<>("Rotates", true, this);

    public Setting<Boolean> swing  = new Setting<>("Swing", true, this);

    public Setting<Boolean> Switch  = new Setting<>("Switch", true, this);

    public Setting<Double> speed = new Setting<>("Delay", (double)0.7, this, 0, 1);


    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();


    public Scaffold(){super("Scaffold",Category.WORLD);}





    private BlockPos pos;

    private boolean packet = false;




    @Override
    public void onTick() {
        super.onTick();
        pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (isAir(pos)) {
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet, mc.player.isSneaking());
            blocksToRender.add(new ScaffoldBlock(BlockUtil.posToVec3d(pos)));
        }

        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }


        if (mc.player == null || mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        mc.player.motionX = calc[0];
        mc.player.motionZ = calc[1];


        if (Switch.getValue() && (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock ) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }


    }




    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        blocksToRender.clear();
    }
}

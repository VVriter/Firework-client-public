package com.firework.client.Features.Modules.World.CoolStuff;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "SourceRemover",category = Module.Category.WORLD)
public class SourceRemover extends Module {

    public Setting<Double> range = new Setting<>("Range", (double)5, this, 1, 6);
    public Setting<Double> delay = new Setting<>("Delay", (double)1.5, this, 0, 10);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Packet", false, this).setVisibility(rotate,true);
    public Setting<Boolean> sneak = new Setting<>("Sneak", false, this).setVisibility(rotate,true);

    public Setting<Enum> switchMode = new Setting<>("Switch", switchModes.Silent, this, switchModes.values());
    public enum switchModes{
        Normal, Multihand, Silent, None
    }

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);

    Timer timer = new Timer();
    @Override public void onEnable() {super.onEnable();
        timer.reset();
    }
    @Override public void onDisable() {super.onDisable();
        timer.reset();
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }



    @Override
    public void onTick() {
        super.onTick();
        for (BlockPos posers : calcPoses()) {
            if (posers != null) {
            if (timer.hasPassedMs(delay.getValue()*100)) {
                doSwitch();
                BlockUtil.placeBlock(posers, EnumHand.MAIN_HAND,rotate.getValue(),packet.getValue(), sneak.getValue());
                timer.reset();
                 }
             }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        for (BlockPos posers : calcPoses()) {
            RenderUtils.drawBoxESP(posers,color.getValue().toRGB(),5,true,false,200,1);
        }
    }


    public List<BlockPos> calcPoses() {
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), true);
        ArrayList<BlockPos> posToFill = new ArrayList<>();
        for (BlockPos pos : positions) {
            if (BlockUtil.getBlock(pos) == Blocks.FLOWING_LAVA || BlockUtil.getBlock(pos) == Blocks.LAVA || BlockUtil.getBlock(pos) == Blocks.FLOWING_WATER || BlockUtil.getBlock(pos) == Blocks.WATER) {
                posToFill.add(pos);
            }
        }
        return posToFill;
    }

     int findBlocksInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    void doSwitch() {
        if (switchMode.getValue(switchModes.Normal)) {
            mc.player.inventory.currentItem = findBlocksInHotbar();
        } else if (switchMode.getValue(switchModes.Multihand)) {
            InventoryUtil.doMultiHand(findBlocksInHotbar()+36,InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switchModes.Silent)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(findBlocksInHotbar()));
        } else {
            //Empty block
        }
    }

}

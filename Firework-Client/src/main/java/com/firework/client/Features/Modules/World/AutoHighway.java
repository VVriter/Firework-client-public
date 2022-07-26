package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "AutoHighway",category = Module.Category.WORLD)
public class AutoHighway extends Module {
    //------------------SETTINGS----------------------//
    public Setting<Double> range = new Setting<>("Range", (double)5, this, 1, 10);
    public Setting<Double> placeDelay = new Setting<>("PlaceDelay", (double)2, this, 1, 10);
    public Setting<Double> breakDelay = new Setting<>("BreakDelay", (double)2, this, 1, 10);

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);
    public Setting<Boolean> sneak = new Setting<>("Sneak", true, this);

    boolean needToBreakBlocksToPlace;
    boolean needToPlaceBlocks;

    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();

    @Override public void onEnable() {super.onEnable();
        breakTimer.reset();
        placeTimer.reset();
    }
    @Override public void onDisable() {super.onDisable();
        breakTimer.reset();
        placeTimer.reset();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> onRender = new Listener<>(event -> {
        if (needToBreakBlocksToPlace) {
            for (BlockPos posToBreak : calcPosesToBreak()) {
                if (breakTimer.hasPassedMs(breakDelay.getValue()*100)) {
                    //BreakBlocksCode
                    //TODO: BlockUtil.breakBlock(posToBreak, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(),sneak.getValue());
                    breakTimer.reset();
                }
            }
        } else if (needToPlaceBlocks) {
            for (BlockPos posToPlace : calcPosesToBreak()) {
                if (placeTimer.hasPassedMs(placeDelay.getValue()*100)) {
                    //PlaceBlocksCode
                    BlockUtil.placeBlock(posToPlace, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(),sneak.getValue());
                    placeTimer.reset();
                }
            }
        }
    });

    public List<BlockPos> calcPosesToBreak() {
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), true);
        ArrayList<BlockPos> posToBreak = new ArrayList<>();
        for (BlockPos pos : positions) {
            //TODO: Calc poses to break
        }
        return posToBreak;
    }


    public List<BlockPos> calcPosesToPlace() {
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), true);
        ArrayList<BlockPos> posToPlace = new ArrayList<>();
        for (BlockPos pos : positions) {
            //TODO: Calc poses to place
        }
        return posToPlace;
    }
}

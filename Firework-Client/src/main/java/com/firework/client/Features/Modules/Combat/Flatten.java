package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "Flatten",
        category = Module.Category.COMBAT,
        description = "Create flat around the player"
)
public class Flatten extends Module {

    public Setting<Mode> mode = new Setting<>("Mode", Mode.Strict, this);
    public Setting<Double> range = new Setting<>("Range", (double)4, this, 1, 6);
    public Setting<Double> delay = new Setting<>("Delay", (double)4, this, 1, 1000);
    public Setting<Double> radius = new Setting<>("Radius", (double)4, this, 1, 6);
    public Setting<Boolean> booleanSetting = new Setting<>("PlockPlacer", false, this).setMode(Setting.Mode.SUB);
    private Setting<BlockPlacer.switchModes> switchModes = new Setting<>("Switchs", BlockPlacer.switchModes.Fast, this).setVisibility(v-> booleanSetting.getValue());

    private Setting<Boolean> rotates = new Setting<>("Rotates", false, this).setVisibility(v-> booleanSetting.getValue());
    private Setting<Boolean> packet = new Setting<>("Packets", true, this).setVisibility(v-> booleanSetting.getValue());

    EntityPlayer target;
    Timer timer = new Timer();
    private BlockPlacer blockPlacer;

    @Override
    public void onEnable() {
        super.onEnable();
        blockPlacer = new BlockPlacer(this, switchModes, rotates, packet);
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        target = PlayerUtil.getClosestTarget(range.getValue());
        if (target == null) return;


        switch (mode.getValue()) {
            case Normal:
                for (BlockPos pos : calcPoses()) {
                    if (timer.hasPassedMs(delay.getValue())) {
                       BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND,true,true,false);
                       timer.reset();
                    }
                }
                break;
            case Strict:

                break;
        }
    });

    @Subscribe
    public Listener<Render3dE> gegra = new Listener<>(e-> {
        if (target == null) return;
        for (BlockPos pos : calcPoses()) {
            RenderUtils.drawBoxESP(pos, Color.CYAN,3,true,true,160,1);
        }
    });

    public List<BlockPos> calcPoses() {
        List<BlockPos> positions = BlockUtil.getSphere(radius.getValue().floatValue(), false, target);
        ArrayList<BlockPos> poses = new ArrayList<>();
        for (BlockPos pos : positions) {
            if (pos.getY() != Math.round(target.posY-1)) continue;
            if (BlockUtil.getBlock(pos) == Blocks.AIR) {
                poses.add(pos);
            } else {
                continue;
            }
        }
        return poses;
    }

    @Override
    public void onToggle() {
        super.onToggle();
        timer.reset();
    }


    public enum Mode {
        Normal, Strict
    }
}

package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "AntiPistonAura",
        category = Module.Category.COMBAT,
        description = "Prevent AutoPiston from your opponent"
)
public class AntiPiston extends Module {

    public Setting<Double> range = new Setting<>("Range", (double)3, this, 1, 6);

    public Setting<Double> delay = new Setting<>("Delay", (double)120, this, 1, 1000);
    public Setting<Boolean> blockBreaker = new Setting<>("BlockBreaker", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockBreaker.mineModes> switchMode = new Setting<>("MineMode", BlockBreaker.mineModes.Classic, this)
            .setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this)
            .setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> raytrace = new Setting<>("Raytrace", true, this)
            .setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> swing = new Setting<>("Swing", true, this)
            .setVisibility(()-> blockBreaker.getValue());
    BlockBreaker breaker;
    Timer timer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
        breaker = new BlockBreaker(this,switchMode,rotate,raytrace,swing);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
        breaker = null;
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener = new Listener<>(e-> {
        for (BlockPos pos : calcPoses()) {
            if (pos == null) return;
            if (timer.hasPassedMs(delay.getValue())) {
                //Mine code
                breaker.breakBlock(pos, mc.player.inventory.getCurrentItem().getItem());
                timer.reset();
            }
        }
    });

    @Subscribe
    public Listener<Render3dE> listener1 = new Listener<>(e-> {
        for(BlockPos pos : calcPoses()) {
            RenderUtils.drawBoxESP(pos, Color.RED,1,true,true,150,1);
        }
    });

    public List<BlockPos> calcPoses() {
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), true);
        ArrayList<BlockPos> poses = new ArrayList<>();
        for (BlockPos pos : positions) {
            if (BlockUtil.getBlock(pos) == Blocks.PISTON || BlockUtil.getBlock(pos) == Blocks.REDSTONE_TORCH) {
                poses.add(pos);
            }
        }
        return poses;
    }
}

package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(
        name = "AutoFarm",
        category = Module.Category.WORLD
)
public class AutoFarm extends Module {

    public Setting<Double> range = new Setting<>("Range", (double)3, this, 1, 6);
    public Setting<Double> delay = new Setting<>("Delay", (double)120, this, 1, 1000);
    public Setting<Boolean> blockBreaker = new Setting<>("BlockBreaker", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockBreaker.mineModes> switchMode = new Setting<>("MineMode", BlockBreaker.mineModes.Classic, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> raytrace = new Setting<>("Raytrace", true, this).setVisibility(v-> blockBreaker.getValue());
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(v-> blockBreaker.getValue());

    public Setting<Boolean> plants = new Setting<>("Plants", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> carrot = new Setting<>("Carrots", true, this).setVisibility(v-> plants.getValue());
    public Setting<Boolean> wheat = new Setting<>("Wheat", true, this).setVisibility(v-> plants.getValue());
    BlockBreaker breaker;
    BlockPlacer placer;

    Timer timer = new Timer();
    Timer timer2 = new Timer();

    @Override
    public void onToggle() {
        super.onToggle();
        timer.reset();
        timer2.reset();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        breaker = new BlockBreaker(this,switchMode,rotate,raytrace,swing);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        breaker = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        super.onTick();
        for (BlockPos pos : calcPoses()) {
            if (timer.hasPassedMs(delay.getValue())) {
                breaker.breakBlock(pos,mc.player.inventory.getCurrentItem().getItem());
                timer.reset();
            }
        }
    });

    public List<BlockPos> calcPoses() {
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue().floatValue(), true);
        ArrayList<BlockPos> poses = new ArrayList<>();
        for (BlockPos pos : positions) {
            if (BlockUtil.getBlock(pos) == Blocks.WHEAT && wheat.getValue()) {
                poses.add(pos);
            } if (BlockUtil.getBlock(pos) == Blocks.CARROTS && carrot.getValue()) {
                poses.add(pos);
            }
        }
        return poses;
    }


    @SubscribeEvent
    public void render(RenderWorldLastEvent e) {
        for (BlockPos pos : calcPoses()) {
            RenderUtils.drawBoxESP(pos, Color.RED,3,true,false,1,1);
        }
    }
}

package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "EchestMiner",
        category = Module.Category.WORLD,
        description = ""
)
public class EchestMiner extends Module {

    public Setting<Integer> radius = new Setting<>("Radius", 3, this, 1, 5);
    BlockPos pos;

    @Override
    public void onEnable() {
        super.onEnable();
        pos = BlockUtil.findBlock(Blocks.AIR,1);
        timer.reset();
        breaker = new BlockBreaker(this,switchMode,rotate,raytrace,swing);
        placeTimer = new Timer();
        placeTimer.reset();

        blockPlacer = new BlockPlacer(this, switchModes, rotates, packet);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        pos = null;
        timer.reset();
        breaker = null;
    }

    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(v-> {
        if (pos != null) {
            RenderUtils.drawBoxESP(pos, Color.RED,1,true,true,150,1);
        }
    });

    public Setting<Boolean> blockBreaker = new Setting<>("BlockBreaker", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockBreaker.mineModes> switchMode = new Setting<>("MineMode", BlockBreaker.mineModes.Classic, this).setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> raytrace = new Setting<>("Raytrace", true, this).setVisibility(()-> blockBreaker.getValue());
    public Setting<Boolean> swing = new Setting<>("Swing", true, this).setVisibility(()-> blockBreaker.getValue());
    public Setting<Double> breakerDelay = new Setting<>("BreakerDelay", (double)3, this, 1, 100).setVisibility(()-> blockBreaker.getValue());
    BlockBreaker breaker;
    Timer timer = new Timer();


    public Setting<Boolean> booleanSetting = new Setting<>("PlockPlacer", false, this).setMode(Setting.Mode.SUB);
    private Setting<BlockPlacer.switchModes> switchModes = new Setting<>("Switchs", BlockPlacer.switchModes.Fast, this).setVisibility(()-> booleanSetting.getValue());

    private Setting<Boolean> rotates = new Setting<>("Rotates", false, this).setVisibility(()-> booleanSetting.getValue());
    private Setting<Boolean> packet = new Setting<>("Packets", true, this).setVisibility(()-> booleanSetting.getValue());
    public Setting<Double> placerDelay = new Setting<>("PlacerDelay", (double)3, this, 1, 100).setVisibility(()-> booleanSetting.getValue());
    private BlockPlacer blockPlacer;

    private Timer placeTimer;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> event = new Listener<>(v-> {
        if (pos != null && BlockUtil.getBlock(pos) == Blocks.ENDER_CHEST) {
            if (timer.hasPassedMs(breakerDelay.getValue())) {
                breaker.breakBlock(pos, Items.DIAMOND_PICKAXE);
                timer.reset();
            }
        }

        if (pos != null && BlockUtil.getBlock(pos) == Blocks.AIR) {
            if (placeTimer.hasPassedMs(placerDelay.getValue())) {
                blockPlacer.placeBlock(pos,Blocks.ENDER_CHEST);
            }
        }
    });

}

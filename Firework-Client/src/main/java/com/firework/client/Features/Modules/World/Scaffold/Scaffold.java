package com.firework.client.Features.Modules.World.Scaffold;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "Scaffold", category = Module.Category.WORLD)
public class Scaffold extends Module {

    Timer timer = new Timer();

    public Setting<Boolean> rotate  = new Setting<>("Rotates", true, this);

    public Setting<Boolean> swing  = new Setting<>("Swing", true, this);

    public Setting<Double> speed = new Setting<>("Delay", (double)0.7, this, 0, 1);
    public Setting<Boolean> Tower  = new Setting<>("Tower", true, this);
    public Setting<Double> TowerDelay = new Setting<>("TowerDelay", (double)102.3, this, 1, 1000).setVisibility(v-> Tower.getValue());
    public Setting<Double> TowerSpeed = new Setting<>("TowerSpeed", (double)1, this, 0, 1).setVisibility(v-> Tower.getValue());

    private List<ScaffoldBlock> blocksToRender = new ArrayList<>();

    private BlockPos pos;

    private boolean packet = false;



    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> onRender = new Listener<>(event -> {
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

        if(Tower.getValue()){
            if (timer.hasPassedMs(TowerDelay.getValue()) && mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.motionY = -0.28f;
                final float towerMotion = 0.41999998688f;
                mc.player.setVelocity(0, towerMotion * TowerSpeed.getValue(), 0);
                this.timer.reset();
            }
        }
    });

    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
        blocksToRender.clear();
    }
}

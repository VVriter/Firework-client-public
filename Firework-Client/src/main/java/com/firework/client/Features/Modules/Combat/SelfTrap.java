package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.TargetUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "SelfTrap",
        category = Module.Category.COMBAT,
        description = "Self traps you"
)
public class SelfTrap extends Module {
    public Setting<Mode> mode = new Setting<>("Mode", Mode.Always, this);
    public Setting<Double> distance = new Setting<>("Distance", (double)3, this, 1, 20).setVisibility(()-> mode.getValue(Mode.Smart));
    public Setting<Double> delay = new Setting<>("Delay", (double)3, this, 1, 1000);
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockPlacer.switchModes> switchType = new Setting<>("SwitchType", BlockPlacer.switchModes.Silent, this).setVisibility(()-> interaction.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(()-> interaction.getValue());
    public Setting<Boolean> packet = new Setting<>("Packet", true, this).setVisibility(()-> interaction.getValue());

    public Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this);

    BlockPlacer placer;
    Timer timer;

    Entity target;

    BlockPos pos;

    int stage;

    int xFancing;
    int zFancing;

    @Override
    public void onEnable() {
        super.onEnable();

        xFancing = 1;
        zFancing = 0;

        placer = new BlockPlacer(this, switchType,rotate,packet);
        timer = new Timer();
        timer.reset();
        stage = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        placer = null;
        timer.reset();
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e->{
        target = TargetUtil.getClosest(true,false,false,false,false,100);
        pos = EntityUtil.getPlayerPos(mc.player);

        switch (mode.getValue()) {
            case Always:
                if (stage == 0) {
                    if (BlockUtil.getBlock(pos.add(xFancing,0,zFancing)) != Blocks.AIR) {
                        stage = 1;
                    }

                    if (BlockUtil.getBlock(pos.add(xFancing,0,zFancing)) == Blocks.AIR) {
                        if (timer.hasPassedMs(delay.getValue())) {
                            placer.placeBlock(pos.add(xFancing,0,zFancing),Blocks.OBSIDIAN);
                            timer.reset();
                            stage = 1;
                        }
                    }
                }


                if (stage == 1) {
                    if (BlockUtil.getBlock(pos.add(xFancing,1,zFancing)) != Blocks.AIR) {
                        stage = 2;
                    }

                    if (BlockUtil.getBlock(pos.add(xFancing,1,zFancing)) == Blocks.AIR) {
                        if (timer.hasPassedMs(delay.getValue())) {
                        placer.placeBlock(pos.add(xFancing,1,zFancing),Blocks.OBSIDIAN);
                        timer.reset();
                        stage = 2;
                        }
                    }
                }

                if (stage == 2) {
                    if (BlockUtil.getBlock(pos.add(xFancing,2,zFancing)) != Blocks.AIR) {
                        stage = 3;
                    }

                    if (BlockUtil.getBlock(pos.add(xFancing,2,zFancing)) == Blocks.AIR) {
                        if (timer.hasPassedMs(delay.getValue())) {
                        placer.placeBlock(pos.add(xFancing,2,zFancing),Blocks.OBSIDIAN);
                        timer.reset();
                        stage = 3;
                        }
                    }
                }

                if (stage == 3) {
                    if (BlockUtil.getBlock(pos.add(0,2,0)) != Blocks.AIR) {
                        if (autoDisable.getValue())
                            onDisable();
                        stage = 0;
                    }

                    if (BlockUtil.getBlock(pos.add(0,2,0)) == Blocks.AIR) {
                        if (timer.hasPassedMs(delay.getValue())) {
                            placer.placeBlock(pos.add(0,2,0),Blocks.OBSIDIAN);
                            timer.reset();
                            if (autoDisable.getValue())
                                onDisable();
                            stage = 0;
                        }
                    }
                }
                break;


            case Smart:
                if (target != null) {
                    if (mc.player.getDistanceSq(target) <= distance.getValue()) {
                        if (stage == 0) {
                            if (BlockUtil.getBlock(pos.add(xFancing,0,zFancing)) != Blocks.AIR) {
                                stage = 1;
                            }

                            if (BlockUtil.getBlock(pos.add(xFancing,0,zFancing)) == Blocks.AIR) {
                                if (timer.hasPassedMs(delay.getValue())) {
                                    placer.placeBlock(pos.add(xFancing,0,zFancing),Blocks.OBSIDIAN);
                                    timer.reset();
                                    stage = 1;
                                }
                            }
                        }


                        if (stage == 1) {
                            if (BlockUtil.getBlock(pos.add(xFancing,1,zFancing)) != Blocks.AIR) {
                                stage = 2;
                            }

                            if (BlockUtil.getBlock(pos.add(xFancing,1,zFancing)) == Blocks.AIR) {
                                if (timer.hasPassedMs(delay.getValue())) {
                                    placer.placeBlock(pos.add(xFancing,1,zFancing),Blocks.OBSIDIAN);
                                    timer.reset();
                                    stage = 2;
                                }
                            }
                        }

                        if (stage == 2) {
                            if (BlockUtil.getBlock(pos.add(xFancing,2,zFancing)) != Blocks.AIR) {
                                stage = 3;
                            }

                            if (BlockUtil.getBlock(pos.add(xFancing,2,zFancing)) == Blocks.AIR) {
                                if (timer.hasPassedMs(delay.getValue())) {
                                    placer.placeBlock(pos.add(xFancing,2,zFancing),Blocks.OBSIDIAN);
                                    timer.reset();
                                    stage = 3;
                                }
                            }
                        }

                        if (stage == 3) {
                            if (BlockUtil.getBlock(pos.add(0,2,0)) != Blocks.AIR) {
                                if (autoDisable.getValue())
                                    onDisable();
                                stage = 0;
                            }

                            if (BlockUtil.getBlock(pos.add(0,2,0)) == Blocks.AIR) {
                                if (timer.hasPassedMs(delay.getValue())) {
                                    placer.placeBlock(pos.add(0,2,0),Blocks.OBSIDIAN);
                                    timer.reset();
                                    if (autoDisable.getValue())
                                        onDisable();
                                    stage = 0;
                                }
                            }
                        }
                    }
                }
                break;
        }
    });

    public enum Mode{
        Always, Smart
    }
}

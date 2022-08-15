package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
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
    public Setting<Double> distance = new Setting<>("Distance", (double)3, this, 1, 20).setVisibility(v-> mode.getValue(Mode.Smart));
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<BlockPlacer.switchModes> switchType = new Setting<>("SwitchType", BlockPlacer.switchModes.Silent, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> packet = new Setting<>("Packet", false, this).setVisibility(v-> interaction.getValue());

    BlockPlacer placer;
    Timer timer;

    Entity target;

    BlockPos pos;

    int stage;

    @Override
    public void onEnable() {
        super.onEnable();
        placer = new BlockPlacer(this, switchType,rotate,packet);
        timer = new Timer();
        timer.reset();
        stage = 1;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        placer = null;
        timer.reset();
        stage = 0;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> ev = new Listener<>(e->{
        target = TargetUtil.getClosest(true,false,false,false,false,100);
        pos = EntityUtil.getPlayerPos(mc.player);
        if (stage == 1) {
            if (BlockUtil.getBlock(pos.add(1,1,0)) != Blocks.AIR) {
                stage = 2;
            }

            if (BlockUtil.getBlock(pos.add(1,1,0)) == Blocks.AIR) {
                placer.placeBlock(pos.add(1,1,0),Blocks.OBSIDIAN);
                stage = 2;
            }
        }

        if (stage == 2) {
            if (BlockUtil.getBlock(pos.add(1,2,0)) != Blocks.AIR) {
                stage = 3;
            }

            if (BlockUtil.getBlock(pos.add(1,2,0)) == Blocks.AIR) {
                placer.placeBlock(pos.add(1,2,0),Blocks.OBSIDIAN);
                stage = 3;
            }
        }

        if (stage == 3) {
            if (BlockUtil.getBlock(pos.add(0,2,0)) != Blocks.AIR) {
                MessageUtil.sendClientMessage("You trapped yourself",-1117);
                onDisable();
            }

            if (BlockUtil.getBlock(pos.add(0,2,0)) == Blocks.AIR) {
                placer.placeBlock(pos.add(0,2,0),Blocks.OBSIDIAN);
                MessageUtil.sendClientMessage("You trapped yourself",-1117);
                onDisable();
            }
        }

    });


    public enum Mode{
        Always, Smart
    }
}

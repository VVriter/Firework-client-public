package com.firework.client.Features.Modules.Combat.Rewrite.Ca;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.CrystalUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "CAura",category = Module.Category.COMBAT)
public class CAura extends Module {


    public Setting<Integer> range = new Setting<>("Range", 5, this, 1, 10);
    public Setting<Boolean> legal = new Setting<>("Legal", true, this);

    public Setting<Integer> minTargetDmg = new Setting<>("MinTargetDmg", 5, this, 1, 20);
    public Setting<Integer> maxSelfDmg = new Setting<>("MaxSelfDmg", 5, this, 1, 20);

    Timer placeTimer;
    Timer breakTimer;

    EntityPlayer target;

    @Override public void onEnable() {super.onEnable();
        placeTimer = new Timer();
        breakTimer = new Timer();
    }

    @Override public void onDisable() {super.onDisable();
        placeTimer = null;
        breakTimer = null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        target = PlayerUtil.getClosestTarget(range.getValue());
    }

    @Override public void onTick() {super.onTick();
        if (target != null) {
            //AutoCrystal code

        }
    }

    public List<BlockPos> calcPoses() {
        List<BlockPos> positions = BlockUtil.getSphere(range.getValue().floatValue(), true);
        ArrayList<BlockPos> placablePoses = new ArrayList<>();
        for (BlockPos pos : positions) {
            if (CrystalUtil.canPlaceCrystal(pos,legal.getValue()) && CrystalUtil.calculateDamage(pos,target) <= minTargetDmg.getValue() ) {
                placablePoses.add(pos);
            }
        }
        return placablePoses;
    }

    @SubscribeEvent public void onRender(RenderWorldLastEvent e) {
        if (target != null) {
            RenderUtils.drawProperBox(target.getPosition(),new Color(203, 3, 3,200));
            for (BlockPos pos : calcPoses()) {
                RenderUtils.drawBoxESP(pos,Color.BLUE,1,true,true,200,1);
            }
        }
    }
}

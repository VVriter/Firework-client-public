package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;

@ModuleManifest(name = "ESP",category = Module.Category.VISUALS)
public class Esp extends Module {
    public Setting<Double> range = new Setting<>("Range", (double)40, this, 1, 100);
    public Setting<Boolean> burrowSubBool = new Setting<>("Burrow", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> enableBurrow = new Setting<>("Enable Burrow", true, this).setVisibility(v-> burrowSubBool.getValue());
    public Setting<Boolean> self = new Setting<>("Self", true, this).setVisibility(v-> burrowSubBool.getValue());
    public Setting<HSLColor> burrowColor = new Setting<>("Burrow Color", new HSLColor(1, 54, 43), this).setVisibility(v-> burrowSubBool.getValue());


    ArrayList<BlockPos> burrowsList = new ArrayList<>();

    @Subscribe
    public Listener<Render3dE> renderer = new Listener<>(e-> {
        burrowsList.clear();
            if (enableBurrow.getValue()) {
                for (EntityPlayer player : mc.world.playerEntities) {
                    final BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
                    if ((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.getValue() && (blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) > 1.5 || this.self.getValue())) {
                        this.burrowsList.add(blockPos);
                    }
                }

                if (burrowsList != null) {
                    for (BlockPos poses : burrowsList) {
                        RenderUtils.drawBoxESP(poses, burrowColor.getValue().toRGB(),1,true,true,100,1);
                    }
                }
            }
        }
    );


}

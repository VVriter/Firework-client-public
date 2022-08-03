package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.init.Blocks;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "Anchor",
        category = Module.Category.COMBAT,
        description = "Stops all movement if you near hole."
)

public class Anchor extends Module {




    @Subscribe
    public Listener<Render3dE> listener = new Listener<>(e-> {
        if (isinHole()) {
            RenderUtils.drawBoxESP(EntityUtil.getFlooredPos(mc.player), Color.CYAN,1,true,false,1,1);
        }
    });



    //Govnocode added
    boolean isinHole() {return first() && second() && three() && fore() && five();}
    boolean first() {return BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(1,0,0)) == Blocks.OBSIDIAN || BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(1,0,0)) == Blocks.BEDROCK;}
    boolean second() {return BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(-1,0,0)) == Blocks.OBSIDIAN || BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(-1,0,0)) == Blocks.BEDROCK;}
    boolean three() {return BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,0,1)) == Blocks.OBSIDIAN || BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,0,1)) == Blocks.BEDROCK;}
    boolean fore() {return BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,0,-1)) == Blocks.OBSIDIAN || BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,-1,-1)) == Blocks.BEDROCK;}
    boolean five() {return BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,-1,0)) == Blocks.OBSIDIAN || BlockUtil.getBlock(EntityUtil.getFlooredPos(mc.player).add(0,-2,0)) == Blocks.BEDROCK;}

}

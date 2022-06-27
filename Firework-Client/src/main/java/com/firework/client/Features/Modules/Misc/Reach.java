package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;

@ModuleManifest(name = "Reach",category = Module.Category.MISC)
public class Reach extends Module {

    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> override = null;
    public static Setting<Double> reach = null;
    public static Setting<Double> add = null;

    public static Setting<Boolean> noMiningTraceBool = null;
    public static Setting<Boolean> pick = null;
    public static Setting<Boolean> gap = null;
    public static Setting<Boolean> obby = null;

    public static boolean noTrace;

    public Reach(){
        enabled = this.isEnabled;
        override = new Setting<>("Override", true, this);
        reach = new Setting<>("Reach", (double)3, this, 1, 15).setVisibility(override,false);
        add = new Setting<>("Add", (double)3, this, 1, 100).setVisibility(override,true);

        noMiningTraceBool = new Setting<>("NoMiningTrace", true, this);
        pick = new Setting<>("Pickaxe", true, this).setVisibility(noMiningTraceBool,true);
        gap = new Setting<>("GoldenApple", true, this).setVisibility(noMiningTraceBool,true);
        obby = new Setting<>("Obsidian", true, this).setVisibility(noMiningTraceBool,true);
    }

    @Override
    public void onTick() {
        super.onTick();
        Item item = mc.player.getHeldItemMainhand().getItem();
        if (item instanceof ItemPickaxe && this.pick.getValue().booleanValue()) {
            this.noTrace = true;
            return;
        }
        if (item == Items.GOLDEN_APPLE && this.gap.getValue().booleanValue()) {
            this.noTrace = true;
            return;
        }
        if (item instanceof ItemBlock) {
            this.noTrace = ((ItemBlock)item).getBlock() == Blocks.OBSIDIAN && this.obby.getValue() != false;
            return;
        }
        this.noTrace = false;
    }
}

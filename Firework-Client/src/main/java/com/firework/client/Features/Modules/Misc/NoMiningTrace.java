package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.TraceEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "NoMiningTrace",
        category = Module.Category.MISCELLANEOUS,
        description = "Removes mining trace"
)
public class NoMiningTrace extends Module {

    public Setting<Mode> mode = new Setting<>("Mode", Mode.Always, this);
    public enum Mode{ Always, Only }
    public Setting<Boolean> pic = new Setting<>("Pickaxe", true, this).setVisibility(()-> mode.getValue(Mode.Only));
    public Setting<Boolean> gap = new Setting<>("Gapple", true, this).setVisibility(()-> mode.getValue(Mode.Only));


    @Subscribe
    public Listener<TraceEvent> event = new Listener<>(e->{
        if (mode.getValue(Mode.Always)) {
            e.setCancelled(true);
        } else if (mode.getValue(Mode.Only)) {
            if (pic.getValue() && mc.player.inventory.getCurrentItem().getItem() instanceof ItemPickaxe) {
                e.setCancelled(true);
            } if (gap.getValue() && mc.player.inventory.getCurrentItem().getItem() == Items.GOLDEN_APPLE) {
                e.setCancelled(true);
            }
        }
    });
}

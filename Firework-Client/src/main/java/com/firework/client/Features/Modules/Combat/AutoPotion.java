package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "AutoPotion",
        category = Module.Category.COMBAT,
        description = "Automatically uses potions"
)
public class AutoPotion extends Module {

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Integer> lookPitch = new Setting<>("Pitch", 90, this, 1, 100);

    public Setting<Integer> delay = new Setting<>("DelayMs", 3, this, 1, 100);

    ItemUser itemUser;
    Timer timer;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new Timer();
        timer.reset();

        itemUser = new ItemUser(this, switchMode, rotate);
    }


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        if (mc.currentScreen == null) {
            usePotion();
        }
    });

    public void usePotion(){
        if(!mc.player.inventory.hasItemStack(new ItemStack(Items.SPLASH_POTION))) return;
        if(timer.hasPassedMs(delay.getValue())) {
            itemUser.useItem(Items.SPLASH_POTION, lookPitch.getValue());
            timer.reset();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer = null;

        itemUser = null;
    }
}

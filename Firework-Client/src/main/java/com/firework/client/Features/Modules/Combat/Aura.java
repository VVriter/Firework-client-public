package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.TickTimer;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.player.EntityPlayer;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Aura",
        category = Module.Category.COMBAT,
        description = "Kills player using sword"
)
public class Aura extends Module {

    public Setting<Double> targetRange = new Setting<>("TargetRange", 4d, this, 0, 6);
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("Switch", ItemUser.switchModes.Fast, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packet = new Setting<>("Packet", true, this);
    public Setting<Boolean> swing = new Setting<>("Swing", true, this);
    public Setting<Integer> delay = new Setting<>("DelayTicks", 20, this, 0, 100);

    ItemUser itemUser;
    TickTimer timer;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new TickTimer();
        itemUser = new ItemUser(this, switchMode, rotate, packet, swing);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        itemUser = null;
        timer.destory();
        timer = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        EntityPlayer target = PlayerUtil.getClosestTarget(targetRange.getValue());
        if(target == null) return;

        if(timer.hasPassedTicks(delay.getValue()) && WeaponUtil.theMostPoweredWeapon(target) != -1){
            itemUser.hitEntity(InventoryUtil.getItemStack(WeaponUtil.theMostPoweredWeapon(target)).getItem(), target);
            timer.reset();
        }
    });

}

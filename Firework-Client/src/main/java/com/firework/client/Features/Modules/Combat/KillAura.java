package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getItemStack;

@ModuleManifest(name = "KillAura", category = Module.Category.COMBAT)
public class KillAura extends Module{

    public Setting<Integer> tmpDelay = new Setting<>("Delay", 20, this, 0, 60);
    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packetSpoof = new Setting<>("Packet", true, this).setVisibility(rotate, true);
    public Setting<Boolean> swing = new Setting<>("Swing", true, this);
    public Setting<Integer> distance = new Setting<>("Distance", 3, this, 0, 6);
    public Setting<Boolean> autoEnable = new Setting<>("AutoEnable", true, this);
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 1, this, 0, 6).setVisibility(autoEnable, true);

    public EntityPlayer target;

    public Setting<Boolean> enabled = this.isEnabled;
    public boolean lastValue;

    public KillAura(){
        updaterManager.registerUpdater(listener1);
        lastValue = this.isEnabled.getValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        lastValue = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.delay = tmpDelay.getValue();
        target = PlayerUtil.getClosestTarget(distance.getValue());

        if(target != null)
            doKillAura();
    }

    public void doKillAura(){
        if (autoSwitch.getValue())
            InventoryUtil.doMultiHand(getItemStack(WeaponUtil.theMostPoweredWeapon(target)).getItem(), InventoryUtil.hands.MainHand);

        if (rotate.getValue())
            RotationUtil.rotate(target.getPositionVector().add(0, 1, 0), packetSpoof.getValue());

        EntityUtil.attackEntity(target, packetSpoof.getValue(), swing.getValue());
    }

    public Updater listener1 = new Updater(){
        @Override
        public void run() {
            if (mc.player == null || mc.world == null) return;
            if (!lastValue) {
                if (autoEnable.getValue()) {
                    this.delay = 20;
                    target = PlayerUtil.getClosest();

                    if (target == null) return;

                    if (target.getPositionVector().distanceTo(mc.player.getPositionVector()) <= targetRange.getValue()) {
                        onEnable();
                    } else if (enabled.getValue()) {
                        enabled.setValue(false);
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            }
        }
    };
}

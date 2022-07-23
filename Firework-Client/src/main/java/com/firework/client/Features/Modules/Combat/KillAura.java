package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Settings.SettingChangeValueEvent;
import com.firework.client.Implementations.Managers.Updater.Updater;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Math.Inhibitator;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.firework.client.Firework.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.getItemStack;

@ModuleManifest(name = "KillAura", category = Module.Category.COMBAT)
public class KillAura extends Module{

    public Setting<Double> tmpDelay = new Setting<>("Delay", (double)20, this, 0, 60);

    public Setting<Boolean> inhibitSubBool = new Setting<>("Inhibit", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> enableInhibit = new Setting<>("EnableInhibit", true, this).setVisibility(v-> inhibitSubBool.getValue());
    public Setting<Integer> startVal = new Setting<>("StartVal", 60, this, 31, 60).setVisibility(v-> inhibitSubBool.getValue());
    public Setting<Integer> endVal = new Setting<>("EndVal", 20, this, 1, 30).setVisibility(v-> inhibitSubBool.getValue());
    public Setting<Double> inhibitSpeed = new Setting<>("InhibitDelay", (double)20, this, 1, 200).setVisibility(v-> inhibitSubBool.getValue());
    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> interaction.getValue());
    public Setting<Boolean> packetSpoof = new Setting<>("Packet", true, this).setVisibility(v-> rotate.getValue() && interaction.getValue());

    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this);
    public Setting<Boolean> swing = new Setting<>("Swing", true, this);
    public Setting<Integer> distance = new Setting<>("Distance", 3, this, 0, 6);
    public Setting<Boolean> autoEnable = new Setting<>("AutoEnable", false, this);
    public Setting<Integer> targetRange = new Setting<>("TargetRange", 1, this, 0, 6).setVisibility(v-> autoEnable.getValue(true));

    public EntityPlayer target;
    public Inhibitator inhibitator;
    public Setting<Boolean> enabled = this.isEnabled;
    public boolean lastValue;


    @Override
    public void onTick() {
        super.onTick();
        if (enableInhibit.getValue()) {
            inhibitator.doInhibitation(tmpDelay,inhibitSpeed.getValue(),startVal.getValue(),endVal.getValue());
        }
    }

    public KillAura(){
        updaterManager.registerUpdater(listener1);
        lastValue = this.isEnabled.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        inhibitator = new Inhibitator();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        lastValue = false;
        inhibitator = null;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.delay = tmpDelay.getValue().intValue();
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
                    target = PlayerUtil.getClosestTarget(targetRange.getValue());

                    if (target != null) {
                        isEnabled.setValue(true);
                        MinecraftForge.EVENT_BUS.register(this);
                    } else if (enabled.getValue()) {
                        enabled.setValue(false);
                        MinecraftForge.EVENT_BUS.unregister(this);
                        lastValue = false;
                    }
                }
            }
        }
    };

    @SubscribeEvent
    public void settingChangeValue(SettingChangeValueEvent event){
        if(event.setting == autoEnable){
            if(!autoEnable.getValue() && target != null && (target.getPositionVector().distanceTo(mc.player.getPositionVector())) <= targetRange.getValue()) {
                onDisable();
            }
        }
    }
}

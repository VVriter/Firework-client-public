package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockUtil;
import com.firework.client.Implementations.Utill.Client.WeaponUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Entity.PlayerUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;

import static com.firework.client.Implementations.Utill.Blocks.BlockUtil.*;
import static com.firework.client.Implementations.Utill.InventoryUtil.doMultiHand;
import static com.firework.client.Implementations.Utill.InventoryUtil.getItemStack;

@ModuleArgs(name = "KillAura", category = Module.Category.COMBAT)
public class KillAura extends Module{

    public Setting<Integer> tmpDelay = new Setting<>("Delay", 20, this, 0, 60);
    public Setting<Integer> distance = new Setting<>("Distance", 3, this, 0, 6);
    public Setting<Boolean> autoSwitch = new Setting<>("AutoSwitch", true, this);
    public Setting<modes> weaponMode = new Setting<>("Weapon", modes.Sword, this, modes.values()).setVisibility(autoSwitch, true);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Boolean> packetSpoof = new Setting<>("Packet", true, this).setVisibility(rotate, true);
    public Setting<Boolean> swing = new Setting<>("Swing", true, this);

    public EntityPlayer target;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.delay = tmpDelay.getValue();
        target = PlayerUtil.getClosest();

        if(target != null) {
            if (target.getPositionVector().distanceTo(mc.player.getPositionVector()) <= distance.getValue()) {
                if (autoSwitch.getValue())
                    InventoryUtil.doMultiHand(getItemStack(WeaponUtil.theMostPoweredSword()).getItem(), InventoryUtil.hands.MainHand);

                if (rotate.getValue())
                    RotationUtil.rotate(target.getPositionVector().add(0, 1, 0), packetSpoof.getValue());

                EntityUtil.attackEntity(target, packetSpoof.getValue(), swing.getValue());
            }
        }
    }

    public enum modes{
        Sword, Axe, Multi
    }
}

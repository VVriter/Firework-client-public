package com.firework.client.Features.Modules.Combat.Rewrite;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "MenderTweakerRewrite",category = Module.Category.COMBAT)
public class MenderTweaker extends Module {
    public Setting<Double> mendDelay = new Setting<>("MendDelay", (double)3, this, 1, 500);

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);

    public Setting<Boolean> autoDisable = new Setting<>("AutoDisable", true, this);

    public Setting<Enum> mendMode = new Setting<>("MendMode", mendModes.Auto, this, mendModes.values());
    public enum mendModes{
        Auto, Manual, CustomBind
    }

    public Setting<Integer> key1 = new Setting<>("CustomKey", Keyboard.KEY_NONE, this).setVisibility(mendMode,mendModes.CustomBind);

    public Setting<Enum> switchMode = new Setting<>("SwitchMode", switchModes.Silent, this, switchModes.values());
    public enum switchModes{
       Normal, Multihand, Silent, None
    }

    @Override
    public void onTick() {
        super.onTick();
        doSwitch();
        doRotates();
        doMend();
    }

    Timer mendTimer1 = new Timer();
    Timer mendTimer2 = new Timer();
    Timer mendTimer3 = new Timer();
    void doMend() {
        if (mendMode.getValue(mendModes.Auto)) {
            if (mendTimer1.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer1.reset();
            }
        } else if (mendMode.getValue(mendModes.Manual) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mendTimer2.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer2.reset();
            }
        }  else if (mendMode.getValue(mendModes.CustomBind) && Keyboard.isKeyDown(key1.getValue())) {
            if (mendTimer3.hasPassedMs(mendDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mendTimer3.reset();
            }
        }
    }

    void doRotates() {
        if (rotate.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));
        }
    }

    void doSwitch() {
        if (switchMode.getValue(switchModes.Normal)) {
            mc.player.inventory.currentItem = MenderInventoryHelper.findExpInHotbar();
        } else if (switchMode.getValue(switchModes.Multihand)) {
            InventoryUtil.doMultiHand (MenderInventoryHelper.findExpInHotbar()+36, InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switchModes.Silent)) {
            if (mendMode.getValue(mendModes.Manual) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            } else if (mendMode.getValue(mendModes.Auto)) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            } else if (mendMode.getValue(mendModes.CustomBind) && Keyboard.isKeyDown(key1.getValue())) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(MenderInventoryHelper.findExpInHotbar()));
            }
        } else if (switchMode.getValue(switchModes.None)) {
            //Do nothing
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
        mendTimer1.reset();
        mendTimer2.reset();
        mendTimer3.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mendTimer1.reset();
        mendTimer2.reset();
        mendTimer3.reset();
    }
}
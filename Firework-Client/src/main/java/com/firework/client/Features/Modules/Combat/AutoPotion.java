package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

@ModuleManifest(name = "AutoPotion",category = Module.Category.COMBAT)
public class AutoPotion extends Module {

    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Double> lookPitch = new Setting<>("Pitch", (double)90, this, 1, 100);

    public Setting<Double> delay = new Setting<>("Delay", (double)3, this, 1, 10);
    private int delay_count;
    int prvSlot;

    Timer timer = new Timer();
    @Override public void onEnable() { super.onEnable();
        delay_count = 0;
        timer.reset();
    }


    @Override public void onTick() {super.onTick();
        if (mc.currentScreen == null) {
            usedXp();
        }
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public void usedXp(){
        int oldPitch = (int)mc.player.rotationPitch;
        prvSlot = mc.player.inventory.currentItem;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        if (rotate.getValue()) {
            mc.player.rotationPitch = lookPitch.getValue().floatValue();
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, lookPitch.getValue().floatValue(), true)); }
        if (timer.hasPassedMs(delay.getValue()*100)) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            timer.reset();
        }
        if (rotate.getValue()) {
            mc.player.rotationPitch = oldPitch; }
        mc.player.inventory.currentItem = prvSlot;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(prvSlot));
    }

    @Override public void onDisable() {super.onDisable();
        timer.reset();
    }

}

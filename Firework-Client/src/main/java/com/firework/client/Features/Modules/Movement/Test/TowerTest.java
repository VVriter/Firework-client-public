package com.firework.client.Features.Modules.Movement.Test;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@ModuleManifest(name = "TowerTest",category = Module.Category.MOVEMENT)
public class TowerTest extends Module {

    public Setting<Double> TowerDelay = new Setting<>("TowerDelay", (double)102.3, this, 1, 1000);
    public Setting<Double> TowerSpeed = new Setting<>("TowerDelay", (double)1, this, 0, 1);

    Timer timer = new Timer();

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timer.reset();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (timer.hasPassedMs(TowerDelay.getValue()) && mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = -0.28f;
            final float towerMotion = 0.41999998688f;
            mc.player.setVelocity(0, towerMotion * TowerSpeed.getValue(), 0);
            this.timer.reset();
        }
    }
}

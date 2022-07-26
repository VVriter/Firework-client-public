package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "AutoBread",category = Module.Category.WORLD)
public class AutoBreed extends Module {


    public Setting<Double> distance = new Setting<>("Distance", (double) 5, this, 1, 10);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);

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

    EntityAnimal toFeed;

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> onRender = new Listener<>(event -> {
        super.onTick();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityAnimal) {
                final EntityAnimal animal = (EntityAnimal) e;
                if (animal.getHealth() > 0) {
                    if (!animal.isChild() && !animal.isInLove() && EntityUtil.getDistanceFromEntityToEntity(animal) <= 4.5f && animal.isBreedingItem(mc.player.inventory.getCurrentItem())) {
                        toFeed = animal;
                        doFeedStuff(rotate.getValue());
                    }
                }
            }
        }
    });

    void doFeedStuff(Boolean rotate) {
        if (rotate) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(toFeed.prevRotationYaw, toFeed.prevRotationPitch, true));
                mc.playerController.interactWithEntity(mc.player, toFeed, EnumHand.MAIN_HAND);
            } else {
                mc.playerController.interactWithEntity(mc.player, toFeed, EnumHand.MAIN_HAND);
        }
    }
}


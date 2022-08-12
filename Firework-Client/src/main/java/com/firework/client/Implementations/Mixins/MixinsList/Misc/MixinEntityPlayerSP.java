package com.firework.client.Implementations.Mixins.MixinsList.Misc;

import com.firework.client.Features.Modules.Combat.Surround;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.Player.PlayerMoveEvent;
import com.firework.client.Implementations.Events.Player.PlayerUpdateEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.firework.beet.Event;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        UpdateWalkingPlayerEvent eventPlayerMotionUpdate = new UpdateWalkingPlayerEvent(Event.Stage.PRE);
        Surround.time = System.nanoTime();
        Firework.eventBus.post(eventPlayerMotionUpdate);
        if (eventPlayerMotionUpdate.isCancelled())
            callbackInfo.cancel();
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onPlayerUpdate(CallbackInfo info) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        MinecraftForge.EVENT_BUS.post(playerUpdateEvent);
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
        PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        Firework.eventBus.post(event);

        if (event.isCancelled()) {
            super.move(type, event.x, event.y, event.z);
            callbackInfo.cancel();
        }
    }
}

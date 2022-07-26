package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.InputUpdateEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "NoSlow", category = Module.Category.MOVEMENT)
public class NoSlow extends Module {

    private Setting<Boolean> strict = new Setting<>("Strict", true, this);
    private Setting<Boolean> items = new Setting<>("Items", true, this);

    public static Setting<Boolean> blocks = null;
    public static Setting<Boolean> soulSand = null;
    public static Setting<Boolean> slimes = null;

    public static Setting<Boolean> enabled = null;


    public NoSlow() {
        enabled = this.isEnabled;
        blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
        soulSand = new Setting<>("SoulSand", true, this).setVisibility(v-> blocks.getValue());
        slimes = new Setting<>("Slimes", true, this).setVisibility(v-> blocks.getValue());
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (items.getValue() && mc.player.isHandActive() && mc.player.getHeldItem(mc.player.getActiveHand()).getItem() instanceof ItemShield) {
            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0 && mc.player.getItemInUseMaxCount() >= 8) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            }
        }
    });

    @Subscribe
    public Listener<InputUpdateEvent> onInput = new Listener<>(event -> {
        if (items.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe /= 0.2f;
            event.getMovementInput().moveForward /= 0.2f;
        }
    });

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer && strict.getValue() && items.getValue()
                && mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, EntityUtil.getFlooredPos(mc.player), EnumFacing.DOWN));
        }
    }
}

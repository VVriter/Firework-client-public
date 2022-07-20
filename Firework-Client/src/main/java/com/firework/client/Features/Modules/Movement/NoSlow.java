package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.PlayerCollideWithBlockEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "NoSlow", category = Module.Category.MOVEMENT)
public class NoSlow extends Module {

    private Setting<Boolean> strict = new Setting<>("Strict", true, this);
    private Setting<Boolean> items = new Setting<>("Items", true, this);

    public static Setting<Boolean> blocks = null;
    public static Setting<Boolean> soulSand = null;
    public Setting<Boolean> slimes = null;


    public NoSlow() {
        blocks = new Setting<>("Blocks", false, this).setMode(Setting.Mode.SUB);
        soulSand = new Setting<>("SoulSand", true, this);
        slimes = new Setting<>("Slimes", true, this);
    }
    @Override
    public void onTick() {
        super.onTick();
        if (items.getValue() && mc.player.isHandActive() && mc.player.getHeldItem(mc.player.getActiveHand()).getItem() instanceof ItemShield) {
            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0 && mc.player.getItemInUseMaxCount() >= 8) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            }
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (items.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe /= 0.2f;
            event.getMovementInput().moveForward /= 0.2f;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer && strict.getValue() && items.getValue()
                && mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, EntityUtil.getFlooredPos(mc.player), EnumFacing.DOWN));
        }
    }
}

package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.ClientTickEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(name = "AutoHotbar", category = Module.Category.COMBAT)
public class AutoHotbar extends Module {

    private int lastSlot = -1;
    private Item lastItem;

    @Override
    public void onDisable() {
        super.onDisable();
        lastSlot = -1;
        lastItem = null;
    }

    @Subscribe
    public Listener<PacketEvent.Receive> onPacketReceive = new Listener<>(event -> {
       if(event.getPacket() instanceof CPacketHeldItemChange){
           CPacketHeldItemChange packet = (CPacketHeldItemChange)event.getPacket();
           lastSlot = packet.getSlotId();
           lastItem = InventoryUtil.getItemStack(lastSlot).getItem();
       }
    });

    @Subscribe
    public Listener<ClientTickEvent> onTick = new Listener<>(clientTickEvent -> {
        if (mc.player == null) {
            return;
        }

        Item held = mc.player.getHeldItemMainhand().getItem();
        if (lastSlot == mc.player.inventory.currentItem && lastItem != held && held == Items.AIR && mc.currentScreen == null) {
            if (!mc.gameSettings.keyBindDrop.isKeyDown() || mc.gameSettings.keyBindDrop.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                int slot = InventoryUtil.getItemSlot(lastItem);
                if (slot != -1) {
                    InventoryUtil.clickSlot(slot);
                    InventoryUtil.clickSlot(mc.player.inventory.currentItem);
                }
            }
        }

        lastSlot = mc.player.inventory.currentItem;
        lastItem = mc.player.getHeldItemMainhand().getItem();
    });
}

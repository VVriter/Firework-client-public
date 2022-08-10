package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Events.WorldClientInitEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Client.Pair;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@ModuleManifest(name = "HotBarRefill", category = Module.Category.MISCELLANEOUS)
public class HotBarRefill extends Module {

    public Setting<Integer> delay = new Setting<>("Delay", 1, this, 1, 10);
    public Setting<Boolean> packetSpoof = new Setting<>("PacketSpoof", true, this);

    Item[] hotbar = new Item[9];
    int remainingDelay;

    @Subscribe
    public Listener<PacketEvent.Send> onPacketSend = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketCloseWindow)
            cacheHotBar();
    });

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck() || mc.player.ticksExisted < 4 || mc.currentScreen instanceof GuiContainer) return;

        --remainingDelay;
        if(remainingDelay > 0) return;
        remainingDelay = delay.getValue();

        refill();
    });

    @SubscribeEvent
    public void onWorldJoin(WorldClientInitEvent event) {
        cacheHotBar();
        remainingDelay = delay.getValue();
    }


    public void cacheHotBar(){
        for(int i = 0; i < 9; i++){
            hotbar[i] = InventoryUtil.getItemStack(i).getItem();
        }
    }

    public void refill(){
        Queue<Pair<Boolean, Integer>> queue = new LinkedList<>();
        for(int i = 0; i < 9; i++){
            ItemStack itemStack = InventoryUtil.getItemStack(i);
            if(hotbar[i] != itemStack.getItem() && hotbar[i] != Items.AIR && InventoryUtil.hasItemNoHotBar(hotbar[i]))
                queue.add(new Pair<>(itemStack.isEmpty(), i));
        }

        //Refills one item stack
        Pair<Boolean, Integer> refill = queue.peek();
        if(refill == null) return;

        if(packetSpoof.getValue())
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.OPEN_INVENTORY));

        int slot = InventoryUtil.getItemSlotNoHotBar(hotbar[refill.two]);
        InventoryUtil.clickSlot(slot);
        InventoryUtil.clickSlot(refill.two);
        if(refill.one)
            InventoryUtil.clickSlot(slot);

        if(packetSpoof.getValue())
            mc.player.connection.sendPacket(new CPacketCloseWindow());
    }
}

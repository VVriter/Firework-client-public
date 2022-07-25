package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Managers.FriendManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.io.File;

@ModuleManifest(name = "MiddleClick", category = Module.Category.MISCELLANEOUS)
public class MiddleClick extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();
    private boolean clicked = false;

    public Setting<Boolean> pearl = new Setting<>("Pearl", true, this);

    public Setting<Boolean> friendsBool = new Setting<>("Friends", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> friend = new Setting<>("Enable", true, this).setVisibility(V-> friendsBool.getValue());
    public Setting<Boolean> notify = new Setting<>("NotifyFriend", true, this).setVisibility(V-> friendsBool.getValue());

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && friend.getValue()) {
                this.onClick();
            }else {
                if(pearl.getValue()){
                throwPearl();}
            }

            this.clicked = true;
        } else {
            this.clicked = false;
        }
    });

    private void throwPearl() {
        int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        boolean offhand = MiddleClick.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;

        if (pearlSlot != -1 || offhand) {
            int oldslot = MiddleClick.mc.player.inventory.currentItem;

            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }

            MiddleClick.mc.playerController.processRightClick(MiddleClick.mc.player, MiddleClick.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }

    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            File theDir = new File(Firework.FIREWORK_DIRECTORY+"Friends/"+entity.getName()+".json");
            if (theDir.exists()){
                theDir.delete();
                MessageUtil.sendClientMessage(entity.getName()+" removed as friend!",false);
            }else {
                FriendManager.parse(entity.getName());
                MessageUtil.sendClientMessage(entity.getName()+" added as friend!",false);
                if(notify.getValue()){
                    mc.player.sendChatMessage("/w "+entity.getName()+" You are added as friend [FIREWORK CLIENT]");
                }
            }
        }
    }

}
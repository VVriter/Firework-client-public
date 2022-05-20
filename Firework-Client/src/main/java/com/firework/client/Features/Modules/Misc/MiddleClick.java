package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MiddleClick extends Module {

    private static Minecraft mc = Minecraft.getMinecraft();
    private boolean clicked = false;

    public Setting<Boolean> pearl = new Setting<>("Pearl", true, this);


    public MiddleClick(){super("MiddleClick",Category.MISC);}



    public void tryToExecute() {
        super.tryToExecute();
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && pearl.getValue()) {
                this.throwPearl();
            }

            this.clicked = true;
        } else {
            this.clicked = false;
        }

    }

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

}

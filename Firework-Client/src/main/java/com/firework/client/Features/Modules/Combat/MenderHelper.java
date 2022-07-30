package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;

@ModuleManifest(
        name = "MenderHelper",
        category = Module.Category.COMBAT,
        description = "Automatically mends/wears your armour"
)
public class MenderHelper extends Module {

    //Auto armor
    public Setting<Boolean> autoArmor = new Setting<>("AutoArmor", true, this);
    public Setting<Boolean> binding = new Setting<>("Binding", true, this).setVisibility(v-> autoArmor.getValue());
    public Setting<Boolean> xCarry = new Setting<>("XCarry", true, this).setVisibility(v-> autoArmor.getValue());
    public Setting<Boolean> packetSpoof = new Setting<>("PacketSpoof", true, this);
    ArrayList<Integer> targetArmorSlots = new ArrayList<>();

    //Mender
    public Setting<Boolean> mend = new Setting<>("Mend", true, this);
    public Setting<mendMode> menderMode = new Setting<>("Mode", mendMode.Auto, this).setVisibility(v-> mend.getValue());
    public enum mendMode{
        Auto, CustomBind
    }

    public Setting<Integer> percent = new Setting<>("Percent", 0, this, 0, 100).setVisibility(v-> mend.getValue() && menderMode.getValue(mendMode.Auto));
    public Setting<Integer> key = new Setting<>("CustomBind", Keyboard.KEY_NONE, this).setVisibility(v-> mend.getValue() && menderMode.getValue(mendMode.CustomBind));
    ArrayList<Integer> targetArmorMendSlots = new ArrayList<>();

    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this).setVisibility(v-> mend.getValue());
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this).setVisibility(v-> mend.getValue());

    public Setting<Integer> delay = new Setting<>("DelayMs", 3, this, 1, 100).setVisibility(v-> mend.getValue());
    public Setting<Integer> lookPitch = new Setting<>("Pitch", 90, this, 1, 90).setVisibility(v-> mend.getValue());

    Timer timer;
    ItemUser itemUser;

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new Timer();
        itemUser = new ItemUser(this, switchMode, rotate);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        itemUser = null;
        timer = null;
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if(fullNullCheck()) return;
        //Armor slots
        int[] armorSlots = new int[]{
                39,38,37,36
        };

        //Process armor slots
        for(Integer slot : armorSlots){
            ItemStack armor = InventoryUtil.getItemStack(slot);
            //Auto armor slots adding
            if(autoArmor.getValue()) {
                if (armor.isEmpty())
                    targetArmorSlots.add(slot);
            }
            //Armor slots to mend adding
            if(mend.getValue() && menderMode.getValue(mendMode.Auto)){
                if(armor.isItemDamaged() && ArmorUtils.getPercentageDurability(armor) <= percent.getValue()) {
                    targetArmorMendSlots.add(slot);
                }
            }
        }

        //Auto armor fun
        if(autoArmor.getValue() && hasArmor()) {
            if (packetSpoof.getValue())
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.OPEN_INVENTORY));

            for (Integer slot : targetArmorSlots) {
                int armorSlot = InventoryUtil.findArmorSlot(InventoryUtil.getEquipmentFromSlot(slot), binding.getValue(), xCarry.getValue());
                if (armorSlot != -1) {
                    InventoryUtil.clickSlot(armorSlot);
                    InventoryUtil.clickSlot(slot);
                    InventoryUtil.clickSlot(armorSlot);
                }
            }
            targetArmorSlots.clear();

            if (packetSpoof.getValue())
                mc.player.connection.sendPacket(new CPacketCloseWindow());
        }

        //Mend fun
        if(mend.getValue()){
            if(menderMode.getValue(mendMode.Auto)) {
                if (!targetArmorMendSlots.isEmpty()) {
                    if (InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE) != -1 && timer.hasPassedMs(delay.getValue())) {
                        itemUser.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
                        timer.reset();
                    }
                    targetArmorMendSlots.clear();
                }
            }else if(menderMode.getValue(mendMode.CustomBind) && Keyboard.isKeyDown(key.getValue())){
                if (InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE) != -1 && timer.hasPassedMs(delay.getValue())) {
                    itemUser.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
                    timer.reset();
                }
            }
        }
    });

    public boolean hasArmor(){
        for(Integer slot : targetArmorSlots){
            int armorSlot = InventoryUtil.findArmorSlot(InventoryUtil.getEquipmentFromSlot(slot), binding.getValue(), xCarry.getValue());
            if(armorSlot != -1)
                return true;
        }
        return false;
    }
}

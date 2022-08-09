package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(name = "MenderHelper", category = Module.Category.COMBAT)
public class MenderHelper extends Module {

    public Setting<mendMode> menderMode = new Setting<>("Mode", mendMode.Auto, this);
    public enum mendMode{
        Auto, CustomBind
    }

    public Setting<Integer> key = new Setting<>("CustomBind", Keyboard.KEY_NONE, this).setVisibility(v-> menderMode.getValue(mendMode.CustomBind));
    public Setting<Integer> percent = new Setting<>("Percent", 0, this, 0, 100).setVisibility(v-> menderMode.getValue(mendMode.Auto));

    public Setting<Integer> delay = new Setting<>("Delay", 1, this, 1, 20);
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Integer> lookPitch = new Setting<>("Pitch", 90, this, 1, 90).setVisibility(v-> rotate.getValue());

    ArrayList<Integer> slotsToMend;

    int remainingDelay;

    ItemUser user;

    @Override
    public void onEnable() {
        super.onEnable();
        slotsToMend = new ArrayList<>();
        remainingDelay = delay.getValue();
        user = new ItemUser(this, switchMode, rotate);
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
       if(fullNullCheck() || mc.player.ticksExisted < 4) return;

       //Delay setting up || (resetting && mending)
       remainingDelay--;
       if(remainingDelay != 0) return;
       remainingDelay = delay.getValue();

        //Iterate through ur armor inv to turn on mending
        if(slotsToMend.isEmpty()){
            for(EntityEquipmentSlot type : EntityEquipmentSlot.values()){
                ItemStack armor = InventoryUtil.getItemStack(InventoryUtil.getSlotFromEquipment(type));

                if(armor.isItemDamaged() && ArmorUtils.getPercentageDurability(armor) <= percent.getValue()) {
                    slotsToMend.add(InventoryUtil.getSlotFromEquipment(type));
                }
            }
        }



        //Mending code part
        if(menderMode.getValue(mendMode.Auto)){
            if(shouldMend())
                user.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
        }else if(menderMode.getValue(mendMode.CustomBind)){
            if(Keyboard.isKeyDown(key.getValue()))
                user.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
        }

        //Removes mended armor slots
        List<Integer> slotsToRemove = new ArrayList<>();

        slotsToMend.forEach(slot -> {
            if(!InventoryUtil.getItemStack(slot).isItemDamaged())
                slotsToRemove.add(slot);
        });

        slotsToMend.removeAll(slotsToRemove);

    });

    public boolean shouldMend(){
        return !slotsToMend.isEmpty()
                && slotsToMend.stream().filter(slot -> InventoryUtil.getItemStack(slot).isItemDamaged()).count() > 0;
    }
}

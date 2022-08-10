package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.ArmorUtils;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.*;

@ModuleManifest(name = "MenderHelper", category = Module.Category.COMBAT)
public class MenderHelper extends Module {

    public Setting<Boolean> armour = new Setting<>("Armour", true, this);
    public Setting<Boolean> tools = new Setting<>("Tools", true, this);

    public Setting<mendMode> menderMode = new Setting<>("Mode", mendMode.Auto, this);
    public enum mendMode{
        Auto, CustomBind
    }

    public Setting<Integer> key = new Setting<>("CustomBind", Keyboard.KEY_NONE, this).setVisibility(v-> menderMode.getValue(mendMode.CustomBind));
    public Setting<Integer> armourPercent = new Setting<>("ArmourPercent", 0, this, 0, 100).setVisibility(v-> menderMode.getValue(mendMode.Auto) && armour.getValue());
    public Setting<Integer> toolsPercent = new Setting<>("ToolsPercent", 0, this, 0, 100).setVisibility(v-> menderMode.getValue(mendMode.Auto) && tools.getValue());

    public Setting<Integer> delay = new Setting<>("Delay", 1, this, 1, 20);
    public Setting<ItemUser.switchModes> switchMode = new Setting<>("SwitchMode", ItemUser.switchModes.Silent, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true, this);
    public Setting<Integer> lookPitch = new Setting<>("Pitch", 90, this, 1, 90).setVisibility(v-> rotate.getValue());

    ArrayList<Integer> armorSlotsToMend;
    Queue<Integer> toolsSlotsToMend;

    int remainingDelay;

    ItemUser user;

    @Override
    public void onEnable() {
        super.onEnable();
        armorSlotsToMend = new ArrayList<>();
        toolsSlotsToMend = new LinkedList<>();
        remainingDelay = delay.getValue();
        user = new ItemUser(this, switchMode, rotate);
    }

    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
       if(fullNullCheck() || mc.player.ticksExisted < 4 || mc.currentScreen instanceof GuiContainer) return;
       if(InventoryUtil.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE) == -1) return;
       //Delay setting up || (resetting && mending)
       remainingDelay--;
       if(remainingDelay != 0) return;
       remainingDelay = delay.getValue();

        int[] armorSlots = new int[]{
                39,38,37,36
        };

        //Iterates through ur armor inv to find valid armour
        if(armorSlotsToMend.isEmpty()){
            for(Integer slot : armorSlots){
                ItemStack armor = InventoryUtil.getItemStack(slot);

                if(armor.isItemDamaged() && ArmorUtils.getPercentageDurability(armor) <= armourPercent.getValue())
                    armorSlotsToMend.add(slot);
            }
        }

        //Iterates through ur hotbar inv to find valid tools
        if(toolsSlotsToMend.isEmpty()){
            for(int i = 0; i < 9; i++){
                ItemStack tool = InventoryUtil.getItemStack(i);
                if(tool.isItemDamaged() && ArmorUtils.getPercentageDurability(tool) <= toolsPercent.getValue())
                    toolsSlotsToMend.add(i);
            }
        }
        //Sorts tools to mend slots list
        toolsSlotsToMend.stream().sorted(Comparator.comparing(slot -> ArmorUtils.getPercentageDurability(InventoryUtil.getItemStack(slot))));


        //Mending code part
        if(menderMode.getValue(mendMode.Auto)){
            boolean shouldMendTools = shouldMendTools();
            boolean shouldMendArmour = shouldMendArmour();
            if(shouldMendTools || shouldMendArmour)
                user.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
        }else if(menderMode.getValue(mendMode.CustomBind)){
            if(Keyboard.isKeyDown(key.getValue()))
                user.useItem(Items.EXPERIENCE_BOTTLE, lookPitch.getValue());
        }

        //Removes mended armor slots
        List<Integer> armourSlotsToRemove = new ArrayList<>();
        for(int slot : armorSlotsToMend){
            ItemStack armor = InventoryUtil.getItemStack(slot);

            if(!armor.isItemDamaged())
                armourSlotsToRemove.add(slot);
        }
        armorSlotsToMend.removeAll(armourSlotsToRemove);

        //Removes mended tools slots
        List<Integer> toolsSlotsToRemove = new ArrayList<>();
        for(int slot : toolsSlotsToMend){
            ItemStack tool = InventoryUtil.getItemStack(slot);

            if(!tool.isItemDamaged())
                toolsSlotsToRemove.add(slot);
        }
        toolsSlotsToMend.removeAll(toolsSlotsToRemove);

    });

    //Returns true if u have valid armor to mend
    public boolean shouldMendArmour(){
        return armour.getValue() && !armorSlotsToMend.isEmpty()
                && armorSlotsToMend.stream().filter(slot -> InventoryUtil.getItemStack(slot).isItemDamaged()).count() > 0;
    }

    //Returns true if u have valid tools to mend and switches to it
    public boolean shouldMendTools(){
        boolean result = tools.getValue() && !toolsSlotsToMend.isEmpty()
                && toolsSlotsToMend.stream().filter(slot -> InventoryUtil.getItemStack(slot).isItemDamaged()).count() > 0;
        if(result)
            InventoryUtil.switchToHotbarSlot(toolsSlotsToMend.peek(), false);
        return result;
    }
}

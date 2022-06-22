package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumHand;

import java.util.Arrays;

@ModuleManifest(name = "AutoCrystal", category = Module.Category.COMBAT)
public class AutoCrystal extends Module {

    public Setting<String> timing = new Setting<>("Timing", "Vanilla", this, Arrays.asList("Vanilla", "Strict","Sequential"));
    public Setting<String> rotates = new Setting<>("Rotates", "Normal", this, Arrays.asList("Normal", "Packet","None"));

    public Setting<Double> placeRange = new Setting<>("PlaceRange", (double)5, this, 1, 10);
    public Setting<Double> breakRange = new Setting<>("BreakRange", (double)5, this, 1, 10);

    public Setting<Double> WallPlaceRange = new Setting<>("WallPlaceRange", (double)5, this, 1, 10);
    public Setting<Double> WallBreakRange = new Setting<>("WallBreakRange", (double)5, this, 1, 10);

    public Setting<Double> placeDelay = new Setting<>("PlaceDelay", (double)20, this, 1, 100);
    public Setting<Double> breakDelay = new Setting<>("BreakDelay", (double)20, this, 1, 100);

    public Setting<Boolean> raytrace = new Setting<>("Raytrace", false, this);

    public Setting<Double> minDmg = new Setting<>("MinimalDamage", (double)5, this, 1, 20);
    public Setting<Double> maxSelfDmg = new Setting<>("MaximalSelfDamage", (double)5, this, 1, 20);


    public Setting<String> switcch = new Setting<>("Switch", "Normal", this, Arrays.asList("Normal", "Silent","None"));

    public Setting<String> swing = new Setting<>("Swing", "Right", this, Arrays.asList("Right", "Left","None"));

    public Setting<HSLColor> color = new Setting<>("RenderColor", new HSLColor(1, 54, 43), this);

    @Override
    public void onTick() {
        super.onTick();





        //Switch--------------------------------------------------------------------------------------------------------------------
        if(switcch.getValue().equals("Normal")){
            if(!mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL)){
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityPlayer && e != mc.player) {
                        if (mc.player.getDistance(e) <= placeRange.getValue() && mc.player.getDistance(e) <= breakRange.getValue() ) {
                            makeNormalSwitch();
                        }
                    }
                }
           }
        }else if(switcch.getValue().equals("Silent")){
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityPlayer && e != mc.player) {
                    if (mc.player.getDistance(e) <= placeRange.getValue() && mc.player.getDistance(e) <= breakRange.getValue() ) {
                        makeSilentSwitch();
                    }
                }
            }
        }else{
            //Okay...
        }

        //Swing---------------------------------------------------------------------------------------------------------------------
        if(swing.getValue().equals("Right")){
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }else if(swing.getValue().equals("Left")){
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }else if(swing.getValue().equals("None")){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }
    }





    public void makeNormalSwitch(){
        if ( (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.END_CRYSTAL)) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemEndCrystal) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }
        }

    public void makeSilentSwitch(){
        //Silent switch code
    }
}

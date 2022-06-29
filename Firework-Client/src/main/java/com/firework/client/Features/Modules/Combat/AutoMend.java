package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

@ModuleManifest(name = "AutoMend",category = Module.Category.COMBAT)
public class AutoMend extends Module {


    public Setting<Enum> mode = new Setting<>("Mode", modes.Stupid, this, modes.values());
    public enum modes{
        Stupid, Smart
    }

    public Setting<Enum> switchMode = new Setting<>("Switch", switches.Silent, this, switches.values());
    public enum switches{
        Normal, Multihand, Silent
    }

    public Setting<Boolean> onSneak = new Setting<>("OnSneak", false, this);
    public Setting<Boolean> rotate = new Setting<>("Rotate", false, this);





  @Override
  public void onTick(){
        super.onTick();

        if(!onSneak.getValue()){
            doSwitch();
        }

        if(mode.getValue(modes.Stupid) && !onSneak.getValue()){
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }

        if(mode.getValue(modes.Stupid) && mc.gameSettings.keyBindSneak.isKeyDown()){
            doSwitch();
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }

  }






    //Silent switch
    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }


    private void doSwitch(){
        if(rotate.getValue()){
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, true));
        }
      if(switchMode.getValue(switches.Normal)){
          mc.player.inventory.currentItem = findExpInHotbar();
      } else if (switchMode.getValue(switches.Multihand)) {
          InventoryUtil.doMultiHand (findExpInHotbar()+36, InventoryUtil.hands.MainHand);
        } else if (switchMode.getValue(switches.Silent)){
          mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
      }
    }
}

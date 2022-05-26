package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class Bypass extends Module {

    public Setting<Boolean> boatPlace = new Setting<>("BoatPlace", true, this);
    public Setting<Boolean> buildHeight  = new Setting<>("BuildHeight", true, this);


    public Bypass(){super("Bypass",Category.WORLD);}

    @Override
    public void onTick(){
       if(boatPlace.getValue()){
           if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBoat && Mouse.isButtonDown(1)) {
               mc.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
               mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(mc.objectMouseOver.getBlockPos(), EnumFacing.SOUTH, EnumHand.MAIN_HAND, 1, 1, 1));
           }
       }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if(buildHeight.getValue()){
        if (mc.player == null) return;
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                final CPacketPlayerTryUseItemOnBlock oldPacket = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                if (oldPacket.getPos().getY() >= 255) {
                    if (oldPacket.getDirection() == EnumFacing.UP) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(oldPacket.getPos(), EnumFacing.DOWN, oldPacket.getHand(), oldPacket.getFacingX(), oldPacket.getFacingY(), oldPacket.getFacingZ()));
                        event.setCanceled(true);
                        }
                    }
                    }
                }
            }


        }



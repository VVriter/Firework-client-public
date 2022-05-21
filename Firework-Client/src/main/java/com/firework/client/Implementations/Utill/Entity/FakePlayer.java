package com.firework.client.Implementations.Utill.Entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public class FakePlayer extends EntityOtherPlayerMP {
    public String nickname;
    public FakePlayer(UUID uuid, String nickname) {
        super(Minecraft.getMinecraft().world, new GameProfile(uuid, nickname));
        this.nickname = nickname;
    }

    public void addMeToWorld() {
        Minecraft.getMinecraft().world.addEntityToWorld(-100, this);
    }

    public void sayAsMe(String message) {
        ITextComponent imessage = new TextComponentString("<" + this.getDisplayNameString() + "> " + message);
        Minecraft.getMinecraft().player.sendMessage(imessage);
    }

    /*@Override
    public void onLivingUpdate() {
        //Do nothing (to prevent crash), because he doesn't have movement controller
    }*/

    @Override
    public void updateRidden() {
        //Do nothing (to prevent crash), because he doesn't have movement controller
    }

    @Override
    public void updateEntityActionState() {
        //Do nothing (to prevent crash), because he doesn't have movement controller
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

}

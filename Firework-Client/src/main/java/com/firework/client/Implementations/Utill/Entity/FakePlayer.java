package com.firework.client.Implementations.Utill.Entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class FakePlayer extends EntityPlayerSP {
    public String nickname;
    public FakePlayer(String nickname) {
        super(Minecraft.getMinecraft(), Minecraft.getMinecraft().world, new NetHandlerPlayClient(Minecraft.getMinecraft(), null, null, new GameProfile(null, nickname)), null, null);
        this.nickname = nickname;
    }

    public void addMeToWorld() {
        Minecraft.getMinecraft().world.addEntityToWorld(getEntityId(), this);
    }

    public void sayAsMe(String message) {
        ITextComponent imessage = new TextComponentString("<" + this.nickname + "> " + message);
        Minecraft.getMinecraft().player.sendMessage(imessage);
    }

    @Override
    public void onLivingUpdate() {
        //Do nothing)
    }

}

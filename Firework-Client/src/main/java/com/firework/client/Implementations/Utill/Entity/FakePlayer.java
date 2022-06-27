package com.firework.client.Implementations.Utill.Entity;

import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public class FakePlayer extends EntityOtherPlayerMP {
    public String nickname;
    public FakePlayer(UUID uuid, String nickname) {
        super(Minecraft.getMinecraft().world, new GameProfile(uuid, nickname));
        this.nickname = nickname;
        this.addMeToWorld();
        this.inventory.addItemStackToInventory(new ItemStack(Items.TOTEM_OF_UNDYING));
    }

    private void addMeToWorld() {
        Minecraft.getMinecraft().world.addEntityToWorld(-100, this);
    }

    private void removeMeFromWorld() {
        Minecraft.getMinecraft().world.removeEntity(this);
    }

    public static void doFakeplayer() {

        String fakePlayerNickname = Minecraft.getMinecraft().getSession().getUsername();
        FakePlayer fakePlayer = new FakePlayer(UUID.fromString("e213ff7e-6c29-4a93-ab83-7a9d03added5"), fakePlayerNickname);
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        fakePlayer.setPosition(localPlayer.posX, localPlayer.posY, localPlayer.posZ);

    }
}

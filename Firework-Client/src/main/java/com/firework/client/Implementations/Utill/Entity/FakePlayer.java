package com.firework.client.Implementations.Utill.Entity;

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
}

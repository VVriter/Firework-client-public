package com.firework.client.Implementations.Utill.Entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;

public class FakePlayer extends EntityPlayerSP {
    public FakePlayer(String nickname) {
        super(Minecraft.getMinecraft(), Minecraft.getMinecraft().world, new NetHandlerPlayClient(Minecraft.getMinecraft(), null, null, new GameProfile(null, nickname)), null, null);
    }

    public void AddMeToWorld() {
        Minecraft.getMinecraft().world.addEntityToWorld(getEntityId(), this);
    }
}

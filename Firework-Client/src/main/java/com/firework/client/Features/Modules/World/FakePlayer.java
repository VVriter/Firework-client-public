package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatisticsManager;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", Category.WORLD, true);
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();
        GameProfile fakePlayerProfile = new GameProfile(null, "FakePlayer");
        NetHandlerPlayClient fakePlayerNetHandler = new NetHandlerPlayClient(Firework.minecraft, null, null, fakePlayerProfile);
        EntityPlayerSP fakePlayer = new EntityPlayerSP(Firework.minecraft, Firework.minecraft.world, fakePlayerNetHandler, null, null);
    }
}

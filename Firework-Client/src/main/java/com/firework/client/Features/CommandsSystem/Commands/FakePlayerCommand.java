package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Entity.FakePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

@CommandManifest(label = "fakeplayer",aliases = "fp")
public class FakePlayerCommand extends Command {
    @Override
    public void execute(String[] args) {
        String fakePlayerNickname = "IAmFakePlayer";
        if(args.length > 0) {
            fakePlayerNickname = args[0];
        }
        FakePlayer fakePlayer = new FakePlayer(fakePlayerNickname);

        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        fakePlayer.addMeToWorld();
        fakePlayer.setPosition(localPlayer.posX, localPlayer.posY, localPlayer.posZ);
        fakePlayer.sayAsMe("Hi, I am " + fakePlayerNickname + "!");
    }
}

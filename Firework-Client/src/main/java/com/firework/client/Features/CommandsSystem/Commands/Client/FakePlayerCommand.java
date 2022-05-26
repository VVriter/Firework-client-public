package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Entity.FakePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.UUID;

@CommandManifest(label = "fakeplayer",aliases = "fp")
public class FakePlayerCommand extends Command {
    @Override
    public void execute(String[] args) {
        //Plays Notification sound
        Notifications.notificate();
        String fakePlayerNickname = "IAmFakePlayer";
        if(args.length > 1) {
            fakePlayerNickname = args[1];
        }
        FakePlayer fakePlayer = new FakePlayer(UUID.fromString("e213ff7e-6c29-4a93-ab83-7a9d03added5"), fakePlayerNickname);

        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;
        fakePlayer.addMeToWorld();
        fakePlayer.setPosition(localPlayer.posX, localPlayer.posY, localPlayer.posZ);
        MessageUtil.sendClientMessage("FakePlayer has been spawned!", false);
    }
}

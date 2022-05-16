package com.firework.client.Features.CommandsSystem.Commands;




import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;


@CommandManifest(label = "dupe",aliases = "dp")
public class DupeCommand extends Command {


    private final Random random = new Random();


    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        WorldClient world = mc.world;
        ;

        if (player == null || mc.world == null) return;

        ItemStack itemStack = player.getHeldItemMainhand();

        if (itemStack.isEmpty()) {
            MessageUtil.sendError("You need to hold an item in hand to dupe!",-1117);
            return;
        }

        int count = random.nextInt(31) + 1;

        for (int i = 0; i <= count; i++) {
            EntityItem entityItem = player.dropItem(itemStack.copy(), false, true);
            if (entityItem != null) world.addEntityToWorld(entityItem.getEntityId(), entityItem);
        }

        int total = count * itemStack.getCount();
        MessageUtil.sendClientMessage("I just used the dupe and got " + total + " " + itemStack.getDisplayName(),-1117);
    }
}


package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

@CommandManifest(label = "give")
public class GiveCommand extends Command {
    @Override
    public void execute(String[] args) {
        if(args.length < 4) {
            MessageUtil.sendClientMessage("Wrong amount of args!", false);
            return;
        }

        System.out.println(args[1]);
        if(args[1] != "item" && args[1] != "block") {
            MessageUtil.sendClientMessage("Item/Block!", false);
            return;
        }

        ItemStack itemStack = new ItemStack(Items.AIR);
        if(args[1] == "item") {
            itemStack = new ItemStack(Item.getItemById(Integer.parseInt(args[2])), Integer.parseInt(args[3]));
        }
        if(args[1] == "block") {
            itemStack = new ItemStack(Block.getBlockById(Integer.parseInt(args[2])), Integer.parseInt(args[3]));
        }

        Minecraft.getMinecraft().player.addItemStackToInventory(itemStack);
    }
}
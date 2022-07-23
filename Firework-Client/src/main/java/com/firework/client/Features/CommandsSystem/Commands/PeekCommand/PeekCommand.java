package com.firework.client.Features.CommandsSystem.Commands.PeekCommand;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;

@CommandManifest(label = "peek")
public class PeekCommand extends Command {
    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            ItemStack stack = PeekCommand.mc.player.getHeldItemMainhand();
            if (stack != null && stack.getItem() instanceof ItemShulkerBox) {
                ToolTips.displayInv(stack, null);
            } else {
                MessageUtil.sendClientMessage("\u00a7cYou need to hold a Shulker in your mainhand.",-1117);
                return;
            }
        }
    }
}

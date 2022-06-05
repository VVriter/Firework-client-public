package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleArgs(name = "ThunderCordExploit",category = Module.Category.WORLD)
public class ThunderHack extends Module {
    @SubscribeEvent
    public void onPacket(PacketEvent.Receive e) {
        BlockPos newPos = null;
        if (e.getPacket() instanceof SPacketEffect) {
            SPacketEffect effect = (SPacketEffect) e.getPacket();
            newPos = effect.getSoundPos();
            if (this.mc.player.getPosition().getDistance(effect.getSoundPos().getX(), effect.getSoundPos().getY(), effect.getSoundPos().getZ()) > 500.0D + this.mc.player.posY) {
                newPos = effect.getSoundPos();
            } else if (e.getPacket() instanceof SPacketSoundEffect) {
                SPacketSoundEffect sound = (SPacketSoundEffect) e.getPacket();

                if (this.mc.player.getPosition().getDistance((int) sound.getX(), (int) sound.getY(), (int) sound.getZ()) > 500.0D + this.mc.player.posY) {
                    newPos = new BlockPos(sound.getX(), sound.getY(), sound.getZ());
                }
            } else if (e.getPacket() instanceof SPacketSpawnGlobalEntity) {
                SPacketSpawnGlobalEntity lightning = (SPacketSpawnGlobalEntity) e.getPacket();

                newPos = new BlockPos(lightning.getX(), lightning.getY(), lightning.getZ());
            }

            if (newPos != null) {
                MessageUtil.sendClientMessage("Thunder struck at: " + TextFormatting.ITALIC + newPos.getX() + TextFormatting.WHITE + ", " + TextFormatting.ITALIC + newPos.getY() + TextFormatting.WHITE + ", " + TextFormatting.ITALIC + newPos.getZ(),-1117);
            }
        }
    }
}

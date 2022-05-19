package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class BridgeBuild extends Module {
    public Setting<Boolean> disableOnJump = new Setting<>("DisableOnJump", false, this);

    public BridgeBuild(){super("BridgeBuild",Category.WORLD);}
    @Override
    public void tryToExecute(){
        super.tryToExecute();
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), mc.world.getBlockState(pos).getBlock() == Blocks.AIR);
        if(disableOnJump.getValue() && Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode())){
        MessageUtil.sendClientMessage("detected jump.. turnin",true);
        this.isEnabled.setValue(false);
        }

    }
}

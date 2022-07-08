package com.firework.client.Implementations.Mixins.MixinsList.Interfaces;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( GuiChat.class )
public interface MixinGuiChat{
    @Accessor(value = "inputField")
    GuiTextField getInputField();


}
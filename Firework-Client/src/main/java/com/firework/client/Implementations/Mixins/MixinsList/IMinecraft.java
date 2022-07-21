package com.firework.client.Implementations.Mixins.MixinsList;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IMinecraft {
    @Accessor(value = "timer")
    Timer getTimer();

    @Accessor(value = "rightClickDelayTimer")
    void setRightClickDelayTimer(int rightClickDelayTimer);

    @Accessor(value = "rightClickDelayTimer")
    int getRightClickDelayTimer();
    @Invoker(value = "rightClickMouse")
    void invokeRightClickMouse();
}
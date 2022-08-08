package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.UI.Hud.Huds.HudGui;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "Hud", category = Module.Category.CLIENT)
public class Hud extends Module{

    public static Setting<Boolean> enabled = null;

    public Hud(){
        this.key.setValue(Keyboard.KEY_L);
        enabled = this.isEnabled;
        enabled.setValue(true);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!(mc.currentScreen instanceof GuiN)) {
            mc.displayGuiScreen(new HudGui());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onEnable();
    }
}

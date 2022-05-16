package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Test extends Module {
    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<String> N = new Setting<>("tSN", "Kill", this, Arrays.asList("Eat", "Kill"));

    public Minecraft mc = Minecraft.getMinecraft();
    public boolean e = true;

    public Test() {
        super("Test", Category.CLIENT);
        isEnabled.setValue(false);
        System.out.println("Module status:" + isEnabled.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //System.out.println("WORK!");
        //System.out.println(isEnabled.getValue());
        if(mc.player == null || mc.world == null) return;

        if(e){
            e = false;
            mc.displayGuiScreen(new Gui());
        }

    }
}

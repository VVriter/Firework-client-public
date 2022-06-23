package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Hud.Huds.Render.MemoryHud.MemoryManager;
import com.firework.client.Implementations.Hud.Huds.Render.PlayerPlayTimeHud.PlayTimeManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

@ModuleManifest(name = "Test", category =  Module.Category.CLIENT)
public class Test extends Module {
    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this);
    public Setting<String> N = new Setting<>("tSN", "Kill", this, Arrays.asList("Eat", "Kill"));
    public Setting<Enum> enumSetting = new Setting<>("tsENUm", TestEnum.un, this, TestEnum.values());
    public enum TestEnum{
        un, lock
    }
    public Minecraft mc = Minecraft.getMinecraft();
    public boolean e = true;

    public Test() {
        isEnabled.setValue(false);
        System.out.println("Module status:" + isEnabled.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();



        mc.player.setGlowing(true);

        if(enumSetting.getValue(TestEnum.lock))
            System.out.println("checked");
    }

    @Override
    public void onTick() {
        MessageUtil.sendClientMessage(String.valueOf(MemoryManager.memory+"//"+MemoryManager.allocatedMemory+"// Max Memory:"+MemoryManager.maxMemory),true);
    }
}

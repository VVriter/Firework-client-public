package com.firework.client.Modules.Client;

import com.firework.client.Modules.Module;
import com.firework.client.Settings.Setting;
import org.lwjgl.Sys;

public class Test extends Module {

    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);

    public Test() {
        super("Test", Category.CLIENT);
        isEnable.setValue(true);
        System.out.println("Module status:" + isEnable.getValue());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        System.out.println(testSetting.getValue());
        testSetting.setValue(!testSetting.getValue());

    }
}

package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Impl.Settings.Setting;

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

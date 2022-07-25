package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Inhibitor;
import ua.firework.beet.Listener;

@ModuleManifest(name = "InhibitorExample", category = Module.Category.CLIENT)
public class InhibitorExample extends Module {
    public Setting<Double> currentValue = new Setting<>("CValue", 0d, this, 0, 50);
    public Setting<Double> speed = new Setting<>("Speed", 0.1d, this, 0, 1);
    public Setting<Double> TargetValue = new Setting<>("Target", 0d, this, 0, 50);

    Inhibitor inhibitor;
    @Override
    public void onEnable() {
        super.onEnable();
        inhibitor = new Inhibitor();
        inhibitor.value = currentValue.getValue();
        inhibitor.setValues(0, TargetValue.getValue(), speed.getValue());
        Firework.eventBus.subscribe(listener1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Firework.eventBus.unsubscribe(listener1);
        inhibitor = null;
    }

    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        inhibitor.update();
        currentValue.setValue(inhibitor.value);
        if(inhibitor.value == TargetValue.getValue())
            System.out.println("Got IT");
    });
}

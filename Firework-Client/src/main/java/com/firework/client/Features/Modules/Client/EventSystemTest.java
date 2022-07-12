package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.TestEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "EventSystem", category = Module.Category.CLIENT)
public class EventSystemTest extends Module {
    private long time;
    @Override
    public void onEnable() {
        super.onEnable();
        time = System.currentTimeMillis();
        MinecraftForge.EVENT_BUS.post(new TestEvent());
    }

    @SubscribeEvent
    public void onTest(TestEvent event){
        System.out.println(System.currentTimeMillis() - time);
    }
}

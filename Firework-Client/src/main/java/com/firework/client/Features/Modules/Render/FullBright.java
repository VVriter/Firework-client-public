package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;

@ModuleManifest(name = "FullBright",category = Module.Category.RENDER)
public class FullBright extends Module {
    @Override
    public void onEnable(){
        super.onEnable();
        new Thread(() -> {
                    try {
                        for(int i=1;i<101;){
                            try{
                                Thread.sleep(50);
                                mc.gameSettings.gammaSetting = i;
                                i++;}catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }}catch (Exception e){
                    }
                }).start();
    }
    @Override
    public void onDisable(){
        super.onDisable();
        mc.gameSettings.gammaSetting = 0;
    }

}

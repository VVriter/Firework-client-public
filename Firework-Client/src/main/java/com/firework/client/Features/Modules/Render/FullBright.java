package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;

@ModuleManifest(name = "FullBright",category = Module.Category.RENDER)
public class FullBright extends Module {
    @Override
    public void onEnable(){
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            for(int i=0;i<500;){
                                try{
                                    Thread.sleep(200);
                                    mc.gameSettings.gammaSetting = i+1;
                                    i++;}catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }}catch (Exception e){

                        }
                    }
                }).start();
    }
    @Override
    public void onDisable(){
        mc.gameSettings.gammaSetting =0;
    }

}

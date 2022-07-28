package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@ModuleManifest(
        name = "Fake F3",
        category = Module.Category.CLIENT,
        description = "Module to prevent cord leak"
)
public class F3Injection extends Module {

    public Setting<Boolean> Coords  = new Setting<>("Cords", true, this);
    public Setting<Boolean> FPS  = new Setting<>("FPS", true, this);
    public Setting<String> fpsmode = new Setting<>("Mode", "Hide", this, Arrays.asList("Hide", "Fake")).setVisibility(v-> FPS.getValue(true));
    public Setting<Boolean> Direction  = new Setting<>("Direction", true, this);
    public Setting<Boolean> Biome  = new Setting<>("Biome ", true, this);


    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.gameSettings.showDebugInfo) {
            for (int i = 0; i < event.getLeft().size(); i++) {
                if (Coords.getValue()) {
                    if (event.getLeft().get(i).contains("Looking"))
                        event.getLeft().set(i, "Looking at a block!");
                    if (event.getLeft().get(i).contains("XYZ") && fpsmode.getValue().equals("Fake")){
                        event.getLeft().set(i, "XYZ: "+mc.player.getPosition().getX()/3*2+325*2/3*2+" "+mc.player.getPosition().getY()/3*2+" "+mc.player.getPosition().getZ()/3*2+325*2/3*2);}
                    if (event.getLeft().get(i).contains("XYZ") && fpsmode.getValue().equals("Hide")){
                        event.getLeft().set(i, "XYZ: NO!");}


                    if (event.getLeft().get(i).contains("Block:"))
                        event.getLeft().set(i, "Block: Hidden!");
                    if (event.getLeft().get(i).contains("Chunk:"))
                        event.getLeft().set(i, "Chunk: Hidden!");
                }
                if (FPS.getValue())
                    if (event.getLeft().get(i).contains("fps"))
                        event.getLeft().set(i, "fps: 0!");
                if (Direction.getValue())
                    if (event.getLeft().get(i).contains("Facing:"))
                        event.getLeft().set(i, "Facing: Hidden!");
                if (Biome.getValue())
                    if (event.getLeft().get(i).contains("Biome:"))
                        event.getLeft().set(i, "Biome: Hidden!");
            }
        }
    }

}

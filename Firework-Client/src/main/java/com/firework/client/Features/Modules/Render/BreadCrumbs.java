package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@ModuleArgs(name = "BreadCrumbs", category = Module.Category.RENDER)
public class BreadCrumbs extends Module {

    public ArrayList<Vec3d> points = new ArrayList<>();

    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(40, 50, 50), this);

    public Setting<Boolean> resetTimer = new Setting<>("ResetTimer", true, this);
    public Setting<Double> timer = new Setting<>("Timer", 11.5d, this, 0, 60).setVisibility(resetTimer, true);

    public Timer timerObj = null;
    public Vec3d lastVec = null;

    @Override
    public void onEnable() {
        super.onEnable();
        timerObj = new Timer();
        timerObj.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        timerObj = null;
        lastVec = null;
        points.clear();
    }

    @Override
    public void onTick() {
        super.onTick();
        if(resetTimer.getValue()) {
            if (timerObj.hasPassed(timer.getValue().doubleValue() * 60)) {
                timerObj.reset();
                points.remove(0);
            }
        }

        if(lastVec == null) lastVec = mc.player.getPositionVector();

        if(lastVec != mc.player.getPositionVector()) {
            points.add(mc.player.getPositionVector());
            lastVec = mc.player.getPositionVector();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        Vec3d lastPoint = null;
        for(Vec3d vec3d : points){
            if(lastPoint == null){
                lastPoint = vec3d;
            }else{
                RenderUtils.drawPoints(lastPoint, vec3d, 5, color.getValue().toRGB());
                lastPoint = vec3d;
            }
        }
    }
}

package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Player.TotemPopEvent;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import com.firework.client.Implementations.Utill.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "PopChams",
        category = Module.Category.VISUALS,
        description = "Cool"
)
public class PopChams extends Module {

    public Setting<Double> alphaDelay = new Setting<>("AlphaDelay", (double)3, this, 1, 10);

    Vec3d posToRender;
    int alpha;
    Timer timer = new Timer();

    @Subscribe
    public Listener<TotemPopEvent> listener = new Listener<>(e-> {
        alpha = 225;
        posToRender = e.getEntity().getPositionVector();
    });

    @Subscribe
    public Listener<Render3dE> listener1 = new Listener<>(e-> {
        if (posToRender != null) {

            BlockPos pos = new BlockPos(Math.round(posToRender.x), Math.round(posToRender.y),Math.round(posToRender.z));
            RenderUtils.drawBoxESP(pos, Color.CYAN,1,true,true,alpha,2);

            if (alpha>1 && timer.hasPassedMs(alphaDelay.getValue())) {
                alpha--;
                timer.reset();
            }
            if (alpha==1) {
                posToRender = null;
                alpha = 225;
            }
        }
    });

}

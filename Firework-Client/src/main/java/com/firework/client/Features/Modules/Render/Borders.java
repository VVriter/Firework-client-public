package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render3dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "Borders",
        category = Module.Category.VISUALS,
        description = "Draws map arts border"
)

public class Borders extends Module {

    public Setting<YVal> yVal = new Setting<>("Y Level", YVal.Player, this);
    public Setting<Double> custom = new Setting<>("Custom", (double)100, this, 1, 255).setVisibility(()-> yVal.getValue(YVal.Custom));
    public Setting<HSLColor> color = new Setting<>("Color", new HSLColor(1, 54, 43), this);
    public enum YVal{
        Bedrock, Player, Custom
    }

    double y;

    @Subscribe
    public Listener<Render3dE> render3dEListener = new Listener<>(e-> {

        switch (yVal.getValue()) {
            case Bedrock:
                y = 1;
                break;
            case Custom:
                y = custom.getValue();
                break;
            case Player:
                y = mc.player.getPositionVector().y;
        }

        RenderUtils.drawLine(new Vec3d(center().x-64,center().y,center().z-64),new Vec3d(center().x+64,center().y,center().z-64),5,color.getValue().toRGB());
        RenderUtils.drawLine(new Vec3d(center().x+64,center().y,center().z+64),new Vec3d(center().x-64,center().y,center().z+64),5,color.getValue().toRGB());

        RenderUtils.drawLine(new Vec3d(center().x+64,center().y,center().z-64),new Vec3d(center().x+64,center().y,center().z+64),5,color.getValue().toRGB());
        RenderUtils.drawLine(new Vec3d(center().x-64,center().y,center().z+64),new Vec3d(center().x-64,center().y,center().z-64),5,color.getValue().toRGB());
    });

    Vec3d center() {
        int xi;
        int zi;

        int i = 128 * (1 << 0);
        int j = MathHelper.floor((mc.player.posX + 64.0D) / (double)i);
        int k = MathHelper.floor((mc.player.posZ + 64.0D) / (double)i);
        xi = j * i + i / 2 - 64;
        zi = k * i + i / 2 - 64;

        return new Vec3d(xi,y,zi);
    }

}

package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Managers.Coords.CoordsManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.ColorUtils;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;

@ModuleManifest(name = "Test", category =  Module.Category.CLIENT)
public class Test extends Module {
    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this);
    public Setting<String> N = new Setting<>("tSN", "Kill", this, Arrays.asList("Eat", "Kill"));
    public Setting<Enum> enumSetting = new Setting<>("tsENUm", TestEnum.un, this, TestEnum.values());
    public enum TestEnum{
        un, lock
    }
    public Minecraft mc = Minecraft.getMinecraft();
    public boolean e = true;
    public BlockPos vec3d;

    public Test() {
        isEnabled.setValue(false);
        System.out.println("Module status:" + isEnabled.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        vec3d = mc.player.getPosition();


        mc.player.setGlowing(true);

        if(enumSetting.getValue(TestEnum.lock))
            System.out.println("checked");
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        //RenderUtils.drawSphere(vec3d, 0.6, 5, new Color(ColorUtils.astolfoColors(100, 100)));
        /*
        BlockRenderBuilder blockRenderBuilder = new BlockRenderBuilder(vec3d)
                .addRenderMode(new RenderMode(RenderMode.renderModes.Fill,
                        Arrays.asList(new Color(ColorUtils.astolfoColors(100, 100)))))
                .render();*/

        BlockRenderBuilder blockRenderBuilder = new BlockRenderBuilder(vec3d)
                .addRenderMode(new RenderMode(RenderMode.renderModes.Fill,
                        Arrays.asList(new Color(ColorUtils.astolfoColors(100, 100)))))
                .addRenderMode(new RenderMode(RenderMode.renderModes.OutLine,
                        Arrays.asList(Color.white, 3f)))
                .render();
    }
}

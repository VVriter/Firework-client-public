package com.firework.client.Features.Modules;

import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.Rounded.RoundRenderUtils;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.Arrays;


@ModuleManifest(name = "Test", category =  Module.Category.CLIENT)
public class Test extends Module {

    public Setting<Boolean> interaction = new Setting<>("Interaction", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this);
    public Setting<String> N = new Setting<>("tSN", "Kill", this, Arrays.asList("Eat", "Kill"));
    public Setting<Enum> enumSetting = new Setting<>("tsENUm", TestEnum.un, this);
    public enum TestEnum{
        un, lock
    }

    @Subscribe
    public Listener<Render2dE> listener = new Listener<>(e-> {
        //RoundRenderUtils.roundedRectangle(new Rectangle(50,50,200,100),20,new Color(200,150,200,255).getRGB());
        //RoundRenderUtils.drawRound(50,50,200,100,10,true,new Color(1,20,250,150));
    });

}

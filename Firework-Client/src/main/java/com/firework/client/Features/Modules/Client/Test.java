package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Blocks.BlockBreaker;
import com.firework.client.Implementations.Utill.Blocks.BlockPlacer;
import com.firework.client.Implementations.Utill.Blocks.BoundingBoxUtil;
import com.firework.client.Implementations.Utill.Entity.EntityUtil;
import com.firework.client.Implementations.Utill.InventoryUtil;
import com.firework.client.Implementations.Utill.Items.ItemUser;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.BlockRenderBuilder;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.PosRenderer;
import com.firework.client.Implementations.Utill.Render.BlockRenderBuilder.RenderMode;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;

import static com.firework.client.Implementations.Utill.InventoryUtil.getItemStack;

@ModuleManifest(name = "Test", category =  Module.Category.CLIENT)
public class Test extends Module {
    public Setting<Boolean> testSetting = new Setting<>("tS", false, this);
    public Setting<Double> td = new Setting<>("tD", (double)3, this, 1, 10);
    public Setting<String> N = new Setting<>("tSN", "Kill", this, Arrays.asList("Eat", "Kill"));
    public Setting<Enum> enumSetting = new Setting<>("tsENUm", TestEnum.un, this, TestEnum.values());
    public enum TestEnum{
        un, lock
    }

    public Setting<PosRenderer.renderModes> renderMode = new Setting<>("SwitchMode", PosRenderer.renderModes.Beacon, this, PosRenderer.renderModes.values());
    public Setting<HSLColor> colorSetting = new Setting<>("colorN", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> gradCo1 = new Setting<>("Color1", new HSLColor(1, 54, 43), this);
    public Setting<HSLColor> gradCo2 = new Setting<>("Color2", new HSLColor(200, 54, 43), this);
    public Setting<Double> width = new Setting<>("Width", (double)3, this, 1, 10);
    PosRenderer posRenderer;
    public Minecraft mc = Minecraft.getMinecraft();
    public boolean e = true;
    public BlockPos vec3d;

    public Item oldItem = null;

    public Test() {
        isEnabled.setValue(false);
        System.out.println("Module status:" + isEnabled.getValue());
    }
    @Override
    public void onEnable() {
        super.onEnable();
        posRenderer = new PosRenderer(this,renderMode);
        vec3d = mc.player.getPosition();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        posRenderer = null;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if (vec3d != null) {
            posRenderer.doRender(
                    vec3d,
                    colorSetting.getValue().toRGB(),
                    width.getValue().floatValue(),
                    gradCo1.getValue().toRGB(),
                    gradCo2.getValue().toRGB()
            );
        }
    }
}

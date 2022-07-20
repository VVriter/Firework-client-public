package com.firework.client.Implementations.UI.Hud.Huds.Render.ArmourHud;

import com.firework.client.Firework;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.UI.Hud.Huds.HudComponent;
import com.firework.client.Implementations.UI.Hud.Huds.HudManifest;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.firework.client.Implementations.Utill.Util.mc;

@HudManifest(name = "ArmourHud")
public class ArmourHud extends HudComponent {

    @Override
    public void initialize() {
        super.initialize();
        this.height = 22;
        this.width = 80;
        this.y = 80;
        this.x = 400;
        initialized = true;
    }

    @Override
    public void draw() {
        super.draw();
        if(!enabled && !(mc.currentScreen instanceof HudGui)) return;
        if(mc.player == null && mc.world == null) return;
        List<ItemStack> armourList = new ArrayList<>(mc.player.inventory.armorInventory);
        Collections.reverse(armourList);

        GL11.glPushMatrix();

        float xSpacing = 0;
        for (ItemStack itemStack : armourList) {
            // We don't want to render stack
            if (itemStack.isEmpty()) {
                xSpacing += 18;
                continue;
            }

            // Render stack
            ItemRenderer.renderItemStack(itemStack, (x + xSpacing), y + 4, true);

            // Get the item's damage percentage
            int itemDamage = (int) (100 - ((1 - (((float) itemStack.getMaxDamage() - (float) itemStack.getItemDamage()) / (float) itemStack.getMaxDamage())) * 100));

            // Scale
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            float scaleFactor = 1 / 0.75f;

            // Render the damage percentage
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(String.valueOf(itemDamage), (float) ((x + xSpacing + (9 - (Firework.customFontManager.getWidth(String.valueOf(itemDamage)) / 2f))) * scaleFactor), y * scaleFactor, -1);

            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

            xSpacing += 18;
        }

        GL11.glPopMatrix();
    }
}
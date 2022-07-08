package com.firework.client.Implementations.Mixins.MixinsList.Guis;

import java.awt.Color;

import com.firework.client.Features.Modules.Client.ToolTips;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = { GuiScreen.class},
        priority = Integer.MAX_VALUE
)
public class MixinGuiScreen {

    RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    FontRenderer fontRenderer;

    public MixinGuiScreen() {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Inject(
            method = { "renderToolTip"},
            at = {             @At("HEAD")},
            cancellable = true
    )
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ToolTips.enabled.getValue() && stack.getItem() instanceof ItemShulkerBox) {
            NBTTagCompound tagCompound = stack.getTagCompound();

            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");

                if (blockEntityTag.hasKey("Items", 9)) {
                    info.cancel();
                    NonNullList nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);

                    ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
                    GlStateManager.enableBlend();
                    GlStateManager.disableRescaleNormal();
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    int x1 = x + 12;
                    int y1 = y - 12;
                    short width = 150;
                    byte height = 60;

                    this.itemRender.zLevel = 300.0F;
                    int bgColor = (new Color(ToolTips.color2.getValue().toRGB().getRed(), ToolTips.color2.getValue().toRGB().getGreen(),ToolTips.color2.getValue().toRGB().getBlue(),ToolTips.scaling.getValue().intValue()).getRGB());

                    this.drawRect((float) x1, (float) y1, (float) (x1 + width), (float) (y1 + height), bgColor);
                    int textColor;

                    textColor = (new Color((int) ToolTips.color.getValue().toRGB().getRed(), (int)ToolTips.color.getValue().toRGB().getGreen(), (int) ToolTips.color.getValue().toRGB().getBlue()).getRGB());


                    int outlineColor;

                    outlineColor = (new Color((int) ToolTips.color.getValue().toRGB().getRed(), (int)ToolTips.color.getValue().toRGB().getGreen(), (int) ToolTips.color.getValue().toRGB().getBlue()).getRGB());

                    RenderUtils2D.drawHorizontalLine(x1 - 1, x1 + width, y1 - 1, outlineColor);
                    RenderUtils2D.drawHorizontalLine(x1 - 1, x1 + width, y1 + height - 1, outlineColor);
                    RenderUtils2D.drawVerticalLine(x1 - 1, y1 - 1, y1 + height - 1, outlineColor);
                    RenderUtils2D.drawVerticalLine(x1 + width, y1 - 1, y1 + height - 1, outlineColor);
                    this.drawCenteredString(stack.getDisplayName(), x1 + width / 2, y1 + 2, textColor);
                    GlStateManager.enableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();

                    for (int i = 0; i < nonnulllist.size(); ++i) {
                        int iX = x + i % 9 * 16 + 11;
                        int iY = y + i / 9 * 16 - 11 + 8;
                        ItemStack itemStack = (ItemStack) nonnulllist.get(i);

                        this.itemRender.renderItemAndEffectIntoGUI(itemStack, iX + 3, iY);
                        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack, iX + 3, iY, (String) null);
                    }

                    RenderHelper.disableStandardItemLighting();
                    this.itemRender.zLevel = 0.0F;
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();
                }
            }
        }

    }

    public void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        Firework.customFontManager.drawString(text, (float) (x - this.fontRenderer.getStringWidth(text) / 2), (float) y, color);
    }
}
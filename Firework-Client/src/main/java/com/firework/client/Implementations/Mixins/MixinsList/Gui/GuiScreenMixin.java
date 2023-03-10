package com.firework.client.Implementations.Mixins.MixinsList.Gui;

import com.firework.client.Features.Modules.Render.NoRender;
import com.firework.client.Features.Modules.Render.ToolTips;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

import static com.firework.client.Features.Modules.Module.mc;

@Mixin(value={GuiScreen.class})
public abstract class GuiScreenMixin {
    @Inject(method={"drawDefaultBackground"}, at={@At(value="HEAD")}, cancellable=true)
    public void fov2(CallbackInfo info) {
        if (NoRender.enabled.getValue() && NoRender.defaultBackground.getValue()) {
            info.cancel();
        }
    }

    @Inject(
            method = { "renderToolTip"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        if (stack.getItem() instanceof ItemShulkerBox && ToolTips.enabled.getValue() && ToolTips.enableShulkers.getValue()) {
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

                    RenderRound.drawGradientRound(x1 - 1, y1, width + 2, height + 3, 5, new Color(7, 222, 190, 255), new Color(7, 222, 190, 255), new Color(8, 122, 227, 255), new Color(8, 122, 227, 255));
                    RenderRound.drawGradientRound(x1 + 1, y1 + 11, width - 2, height - 10, 2.5f, new Color(118, 7, 222, 255), new Color(158, 67, 211, 255), new Color(177, 6, 182, 255), new Color(227, 8, 183, 255));
                    Firework.customFontManager.drawCenteredString(stack.getDisplayName(), x1 - 1 + ((width + 2) / 2), y1 + 1, Color.WHITE.getRGB());


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

                        if (itemStack.getCount() == 1) {
                            RenderUtils2D.renderItemStack(itemStack, new Point(iX + 3, iY + 2), "");
                        } else  {
                            RenderUtils2D.renderItemStack(itemStack, new Point(iX + 3, iY + 2), String.valueOf(itemStack.getCount()));
                        }
                    }


                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();

                }
            }
        }


        if (stack.getItem() instanceof ItemMap && ToolTips.enabled.getValue() && ToolTips.enableMaps.getValue()) {
            MapData mapdata = ((ItemMap) stack.getItem()).getMapData(stack, mc.world);
            if (mapdata == null) return;
            info.cancel();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.translate(x, y - 72D, 0D);
            GlStateManager.scale(0.5D, 0.5D, 1.0D);
            mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }

    /**
     * @author gegra
     * @reason bebra
     */
    @Overwrite
    protected void handleComponentHover(ITextComponent component, int x, int y)
    {
        if (component != null && component.getStyle().getHoverEvent() != null)
        {
            HoverEvent hoverevent = component.getStyle().getHoverEvent();

            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack itemstack = ItemStack.EMPTY;

                try
                {
                    NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());

                    if (nbtbase instanceof NBTTagCompound)
                    {
                        itemstack = new ItemStack((NBTTagCompound)nbtbase);
                    }
                }
                catch (NBTException var9)
                {

                }

                if (itemstack.isEmpty())
                {
                   // this.drawHoveringText(TextFormatting.RED + "Invalid Item!", x, y);
                }
                else
                {
                   // this.renderToolTip(itemstack, x, y);
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY)
            {
                if (mc.gameSettings.advancedItemTooltips)
                {
                    try
                    {
                        NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                        List<String> list = Lists.<String>newArrayList();
                        list.add(nbttagcompound.getString("name"));

                        if (nbttagcompound.hasKey("type", 8))
                        {
                            String s = nbttagcompound.getString("type");
                            list.add("Type: " + s);
                        }

                        list.add(nbttagcompound.getString("id"));
                      //  this.drawHoveringText(list, x, y);
                    }
                    catch (NBTException var8)
                    {
                       // this.drawHoveringText(TextFormatting.RED + "Invalid Entity!", x, y);
                    }
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                RenderRound.drawRound(x,y-10,Firework.customFontManager.getWidth(hoverevent.getValue().getFormattedText())+2,10,1,true,new Color(147, 43, 43,255));
                Firework.customFontManager.drawString(hoverevent.getValue().getFormattedText(),x+1,y-10,Color.WHITE.getRGB());
            }

            GlStateManager.disableLighting();
        }
    }
}
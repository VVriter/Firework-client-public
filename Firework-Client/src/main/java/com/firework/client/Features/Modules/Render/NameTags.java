package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.MathUtil;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;

@ModuleManifest(name = "NameTags",category = Module.Category.VISUALS)
public class NameTags extends Module {

    public Setting<Boolean> armor = new Setting<>("armor", false, this);
    public Setting<Boolean> health = new Setting<>("health", false, this);
    public Setting<Boolean> ping = new Setting<>("ping", false, this);
    public Setting<Boolean> gamemode = new Setting<>("gamemode", false, this);
    public Setting<Boolean> durability = new Setting<>("durability", false, this);
    public Setting<Boolean> itemname = new Setting<>("itemname", false, this);
    public Setting<Double> scaling = new Setting<>("scaling", (double)3, this, 1, 5);
    public Setting<HSLColor> color = new Setting<>("colorN", new HSLColor(1, 54, 43), this);




    int ri = 0;

    //Canceles vanilla nametags
    @SubscribeEvent
    public void onRenderNametagCancel(RenderLivingEvent.Specials.Pre e) {
        e.setCanceled(true);
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        this.ri = 0;
       // this.rutil.onRender();
        if (this.ri >= 355) {
            this.ri = 0;
        }

        Iterator iterator = mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();
            Entity entity = (Entity) o;

            if (entity instanceof EntityPlayer && entity.isEntityAlive()) {
                double x = this.interpolate(entity.lastTickPosX, entity.posX, e.getPartialTicks()) - mc.getRenderManager().viewerPosX;
                double y = this.interpolate(entity.lastTickPosY, entity.posY, e.getPartialTicks()) - mc.getRenderManager().viewerPosY;
                double z = this.interpolate(entity.lastTickPosZ, entity.posZ, e.getPartialTicks()) - mc.getRenderManager().viewerPosZ;

                if (!entity.getName().equalsIgnoreCase(mc.player.getName())) {
                    this.renderNameTag((EntityPlayer) entity, x, y, z, e.getPartialTicks());
                }
            }
        }
    }


    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
        Entity camera = mc.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;

        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        int width = mc.fontRenderer.getStringWidth(this.getDisplayName(player)) / 2;
        double scale = 0.0018D + this.scaling.getValue() * 0.001D * distance;

        if (distance <= 8.0D) {
            scale = 0.0245D;
        }

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float f = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;

        GlStateManager.rotate(mc.getRenderManager().playerViewX, f, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GL11.glDisable(2929);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        drawBorderedRect((double) (-width - 2), (double) (-(mc.fontRenderer.FONT_HEIGHT + 1)), (double) ((float) width + 2.0F), 1.5D, 1.600000023841858D, 1996488704, this.color.getValue().toRGB().getRGB());
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glColor4f(1.0F, 10.0F, 1.0F, 1.0F);
        mc.fontRenderer.drawStringWithShadow(this.getDisplayName(player), (float) (-width), (float) (-(mc.fontRenderer.FONT_HEIGHT - 1)), this.getDisplayColour(player));
        GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);
        if (this.armor.getValue() || this.durability.getValue() && !this.armor.getValue()) {
            GlStateManager.pushMatrix();
            int xOffset = 0;

            if ((player.getHeldItemMainhand().getItem() == null || player.getHeldItemOffhand().getItem() != null) && (player.getHeldItemMainhand().getItem() != null || player.getHeldItemOffhand().getItem() == null)) {
                if (player.getHeldItemMainhand().getItem() != null && player.getHeldItemOffhand().getItem() != null) {
                    xOffset = -8;
                }
            } else {
                xOffset = -4;
            }

            int index;

            for (index = 3; index >= 0; --index) {
                ItemStack armorStacks = (ItemStack) player.inventory.armorInventory.get(index);

                if (armorStacks != null && armorStacks.getItem() != Items.AIR) {
                    xOffset -= 8;
                }
            }

            ArrayList arraylist = new ArrayList();
            ItemStack renderStack;

            if (player.inventory.armorInventory != null) {
                Iterator stacks = player.inventory.armorInventory.iterator();

                while (stacks.hasNext()) {
                    renderStack = (ItemStack) stacks.next();
                    if (renderStack != null && !renderStack.getItem().equals(Items.AIR)) {
                        arraylist.add(renderStack);
                    }
                }
            }

            ArrayList arraylist1 = new ArrayList();

            arraylist1.addAll(player.inventory.armorInventory);
            if (player.getHeldItemMainhand() != null) {
                arraylist1.add(player.getHeldItemMainhand().copy());
            }

            if (player.getHeldItemOffhand() != null) {
                arraylist1.add(player.getHeldItemOffhand().copy());
            }

            if (player.getHeldItemMainhand() != null) {
                xOffset -= 8;
                renderStack = player.getHeldItemMainhand().copy();
                if (!renderStack.getItem().equals(Items.AIR)) {
                    this.renderItemStack(arraylist1, renderStack, xOffset, -(this.getEnchantSpace(arraylist1) + 26) + 26 + 10);
                    if (arraylist.isEmpty()) {
                        xOffset += 22;
                    } else {
                        xOffset += 16;
                    }
                }
            }

            for (index = arraylist.size() - 1; index >= 0; --index) {
                renderStack = (ItemStack) arraylist.get(index);
                if (renderStack != null) {
                    ItemStack armourStack = renderStack.copy();

                    if (!armourStack.getItem().equals(Items.AIR)) {
                        if (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor || armourStack.getItem().equals(Items.ELYTRA)) {
                            this.renderItemStack(arraylist1, armourStack, xOffset, -(this.getEnchantSpace(arraylist1) + 26) + 26 + 10);
                        }

                        if (arraylist.get(0) == renderStack) {
                            xOffset += 24;
                        } else {
                            xOffset += 16;
                        }
                    }
                }
            }

            if (player.getHeldItemOffhand() != null) {
                xOffset -= 8;
                renderStack = player.getHeldItemOffhand().copy();
                if (!renderStack.getItem().equals(Items.AIR)) {
                    this.renderItemStack(arraylist1, renderStack, xOffset, -(this.getEnchantSpace(arraylist1) + 26) + 26 + 10);
                    xOffset += 16;
                }
            }

            GlStateManager.popMatrix();
        }

        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.popMatrix();
    }

    public static void drawBorderedRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        enableGL2D();
        RenderUtils.fakeGuiRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderUtils.fakeGuiRect(x + width, y, x1 - width, y + width, borderColor);
        RenderUtils.fakeGuiRect(x, y, x + width, y1, borderColor);
        RenderUtils.fakeGuiRect(x1 - width, y, x1, y1, borderColor);
        RenderUtils.fakeGuiRect(x + width, y1 - width, x1 - width, y1, borderColor);
        disableGL2D();
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public int getEnchantSpace(ArrayList items) {
        int biggestEncCount = 0;
        Iterator iterator = items.iterator();

        while (iterator.hasNext()) {
            ItemStack i = (ItemStack) iterator.next();
            NBTTagList enchants = i.getEnchantmentTagList();

            if (enchants != null && enchants.tagCount() > biggestEncCount) {
                biggestEncCount = enchants.tagCount();
            }
        }

        return biggestEncCount * 8;
    }

    public int getHighestEncY(ArrayList items) {
        return this.getEnchantSpace(items);
    }

    public String getGMText(EntityPlayer p) {
        return p.isCreative() ? "C" : (p.isSpectator() ? "I" : (!p.isAllowEdit() && !p.isSpectator() ? "A" : (!p.isCreative() && !p.isSpectator() && p.isAllowEdit() ? "S" : "")));
    }

    public int getPing(EntityPlayer p) {
        int ping = 0;

        try {
            ping = (int) MathUtil.clamp((float) mc.getConnection().getPlayerInfo(p.getUniqueID()).getResponseTime(), 0.0F, 300.0F);
        } catch (NullPointerException nullpointerexception) {
            ;
        }

        return ping;
    }

    private void renderItemStack(ArrayList stacks, ItemStack stack, int x, int y) {
        int enchantY = 2 - this.getEnchantSpace(stacks) - 24;
        int armorY = 2 - this.getEnchantSpace(stacks) / 2 - 14;

        if (armorY >= -26) {
            armorY = -26;
        }

        if (enchantY >= -50) {
            enchantY = -50;
        }

        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        if (this.armor.getValue()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, armorY);
        }

        if (this.armor.getValue()) {
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, armorY);
        }

        if (this.armor.getValue()) {
            mc.getRenderItem().zLevel = 0.0F;
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, enchantY);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y;

        if (!this.armor.getValue()) {
            y += 10;
        }

        int index;

        if ((stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemElytra) && this.durability.getValue()) {
            float enchants = (float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage() * 100.0F;

            index = (int) Math.min(enchants, 100.0F);
            mc.fontRenderer.drawStringWithShadow(index + "%", (float) (x * 2), (float) (y - 10), stack.getItem().getRGBDurabilityForDisplay(stack));
        }

        if (stack.getItem() != null && !(stack.getItem() instanceof ItemAir) && this.armor.getValue()) {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            if (nbttaglist != null) {
                for (index = 0; index < nbttaglist.tagCount(); ++index) {
                    short id = nbttaglist.getCompoundTagAt(index).getShort("id");
                    short level = nbttaglist.getCompoundTagAt(index).getShort("lvl");
                    Enchantment enc = Enchantment.getEnchantmentByID(id);

                    if (enc != null) {
                        String encName = enc.isCurse() ? enc.getTranslatedName(level).substring(11).substring(0, 1).toLowerCase() : enc.getTranslatedName(level).substring(0, 3).toLowerCase();

                        if (!String.valueOf(level).equalsIgnoreCase("1") && !enc.isCurse()) {
                            encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase() + String.valueOf(level);
                        } else if (String.valueOf(level).equalsIgnoreCase("1") && !enc.isCurse()) {
                            encName = enc.getTranslatedName(level).substring(0, 3).toLowerCase();
                        }

                        if (enc.isCurse()) {
                            encName = "Van";
                        }

                        encName = encName.substring(0, 1).toUpperCase() + encName.substring(1);
                        mc.fontRenderer.drawString(encName, x * 2, enchantmentY, -1);
                        enchantmentY += 8;
                    }
                }
            }
        }

        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
          //  GuiUtil.drawString("God", x * 2, enchantmentY, -6416384);
        }

    }

    private String getDisplayName(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();

        if (!this.health.getValue()) {
            return name;
        } else {
            float health = player.getHealth() + player.getAbsorptionAmount();

            if (health <= 0.0F) {
                health = 1.0F;
            }

            TextFormatting color;

            if (health > 18.0F) {
                color = TextFormatting.GREEN;
            } else if (health > 16.0F) {
                color = TextFormatting.DARK_GREEN;
            } else if (health > 12.0F) {
                color = TextFormatting.YELLOW;
            } else if (health > 8.0F) {
                color = TextFormatting.GOLD;
            } else if (health > 5.0F) {
                color = TextFormatting.RED;
            } else {
                color = TextFormatting.DARK_RED;
            }

            String gm = "";
            String p = "";

            if (this.gamemode.getValue()) {
                gm = gm + " [" + this.getGMText(player) + "]";
            }

            if (this.ping.getValue()) {
                p = p + " " + this.getPing(player) + "ms";
            }

            float totalHealth = player.getHealth() + player.getAbsorptionAmount();
            int pHealth = (int) Math.ceil((double) totalHealth);

            if (pHealth <= 0) {
                pHealth = 1;
            }

            name = name + gm + p + color + " " + pHealth;
            return name;
        }
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -1;
            if (player.isInvisible()) {
                colour = -3593216;
            } else if (player.isSneaking()) {
                colour = -3593216;
            }

            return colour;
        }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }

}

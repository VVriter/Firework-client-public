package com.firework.client.Implementations.Events.Render;

import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import ua.firework.beet.Event;

import java.util.ArrayList;

public class RenderGameOverlay extends Event {
    public float getPartialTicks()
    {
        return partialTicks;
    }

    public ScaledResolution getResolution()
    {
        return resolution;
    }

    public RenderGameOverlayEvent.ElementType getType()
    {
        return type;
    }

    public static enum ElementType
    {
        ALL,
        HELMET,
        PORTAL,
        CROSSHAIRS,
        BOSSHEALTH, // All boss bars
        BOSSINFO,    // Individual boss bar
        ARMOR,
        HEALTH,
        FOOD,
        AIR,
        HOTBAR,
        EXPERIENCE,
        TEXT,
        HEALTHMOUNT,
        JUMPBAR,
        CHAT,
        PLAYER_LIST,
        DEBUG,
        POTION_ICONS,
        SUBTITLES,
        FPS_GRAPH,
        VIGNETTE
    }

    private final float partialTicks;
    private final ScaledResolution resolution;
    private final RenderGameOverlayEvent.ElementType type;

    public RenderGameOverlay(float partialTicks, ScaledResolution resolution)
    {
        this.partialTicks = partialTicks;
        this.resolution = resolution;
        this.type = null;
    }

    public RenderGameOverlay(RenderGameOverlayEvent parent, RenderGameOverlayEvent.ElementType type)
    {
        this.partialTicks = parent.getPartialTicks();
        this.resolution = parent.getResolution();
        this.type = type;
    }

    public static class BossInfo extends RenderGameOverlayEvent.Pre
    {
        private final BossInfoClient bossInfo;
        private final int x;
        private final int y;
        private int increment;
        public BossInfo(RenderGameOverlayEvent parent, ElementType type, BossInfoClient bossInfo, int x, int y, int increment)
        {
            super(parent, type);
            this.bossInfo = bossInfo;
            this.x = x;
            this.y = y;
            this.increment = increment;
        }

        /**
         * @return The {@link BossInfoClient} currently being rendered
         */
        public BossInfoClient getBossInfo()
        {
            return bossInfo;
        }

        /**
         * @return The current x position we are rendering at
         */
        public int getX()
        {
            return x;
        }

        /**
         * @return The current y position we are rendering at
         */
        public int getY()
        {
            return y;
        }

        /**
         * @return How much to move down before rendering the next bar
         */
        public int getIncrement()
        {
            return increment;
        }

        /**
         * Sets the amount to move down before rendering the next bar
         * @param increment The increment to set
         */
        public void setIncrement(int increment)
        {
            this.increment = increment;
        }
    }

    public static class Text extends RenderGameOverlayEvent.Pre
    {
        private final ArrayList<String> left;
        private final ArrayList<String> right;
        public Text(RenderGameOverlayEvent parent, ArrayList<String> left, ArrayList<String> right)
        {
            super(parent, ElementType.TEXT);
            this.left = left;
            this.right = right;
        }

        public ArrayList<String> getLeft()
        {
            return left;
        }

        public ArrayList<String> getRight()
        {
            return right;
        }
    }

    public static class Chat extends RenderGameOverlayEvent.Pre
    {
        private int posX;
        private int posY;

        public Chat(RenderGameOverlayEvent parent, int posX, int posY)
        {
            super(parent, ElementType.CHAT);
            this.setPosX(posX);
            this.setPosY(posY);
        }

        public int getPosX()
        {
            return posX;
        }

        public void setPosX(int posX)
        {
            this.posX = posX;
        }

        public int getPosY()
        {
            return posY;
        }

        public void setPosY(int posY)
        {
            this.posY = posY;
        }
    }
}

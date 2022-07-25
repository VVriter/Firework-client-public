package com.firework.client.Implementations.Events;

import net.minecraft.client.gui.GuiScreen;
import ua.firework.beet.Event;

public class GuiOpenEvent extends Event {
    private GuiScreen gui;
    public GuiOpenEvent(GuiScreen gui)
    {
        this.setGui(gui);
    }

    public GuiScreen getGui()
    {
        return gui;
    }

    public void setGui(GuiScreen gui)
    {
        this.gui = gui;
    }
}

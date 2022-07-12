package AltManagerV2.gui;

import AltManagerV2.Account;
import AltManagerV2.AccountManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiAdd extends GuiAbstractInput {
    public GuiAdd(final GuiScreen previousScreen) {
        super(previousScreen, "Add");
    }

    @Override
    public void complete() {
        if (getPassword().isEmpty()) {
            AccountManager.getAccounts().add(new Account(getUsername()));
        } else {
            AccountManager.getAccounts().add(new Account(getUsername(), getPassword()));
        }
    }
}
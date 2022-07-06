package com.firework.client.Features.AltManagerV2.gui;

import com.firework.client.Features.AltManagerV2.Account;
import com.firework.client.Features.AltManagerV2.AccountManager;
import com.firework.client.Features.AltManagerV2.auth.LegacyAuth;
import com.firework.client.Features.AltManagerV2.auth.MojangAuth;
import com.firework.client.Features.AltManagerV2.auth.SessionManager;
import com.firework.client.Features.AltManagerV2.utils.Notification;
import com.firework.client.Features.AltManagerV2.utils.TextFormatting;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiAccountManager extends GuiScreen {
    private final GuiScreen previousScreen;
    private final boolean blockMicrosoftButton;
    private final long startTime;

    private GuiButton loginButton = null;
    private GuiButton editButton = null;
    private GuiButton importButton = null;
    private GuiButton deleteButton = null;
    private GuiButton cancelButton = null;
    private GuiButton microsoftButton = null;
  //  private GuiAccountList guiAccountList = null;
    private String status = null;
    private ExecutorService executor = null;
    private CompletableFuture<Void> task = null;
    private int selectedAccount = -1;

    public GuiAccountManager(final GuiScreen previousScreen, final boolean blockMicrosoftButton) {
        this.previousScreen = previousScreen;
        this.blockMicrosoftButton = blockMicrosoftButton;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        // Top Row
        buttonList.add(loginButton = new GuiButton(
                0, width / 2 - 150 - 4, height - 52, 100, 20, "Login"
        ));
        buttonList.add(editButton = new GuiButton(
                1, width / 2 - 50, height - 52, 100, 20, "Edit"
        ));
        buttonList.add(new GuiButton(
                2, width / 2 + 50 + 4, height - 52, 100, 20, "Add"
        ));

        // Bottom Row
        buttonList.add(importButton = new GuiButton(
                3, width / 2 - 150 - 4, height - 28, 100, 20, "Import"
        ));
        buttonList.add(deleteButton = new GuiButton(
                4, width / 2 - 50, height - 28, 100, 20, "Delete"
        ));
        buttonList.add(cancelButton = new GuiButton(
                5, width / 2 + 50 + 4, height - 28, 100, 20, "Cancel"
        ));

        // Microsoft Authentication
        buttonList.add(microsoftButton = new GuiButton(
                6, width - 106, 6, 100, 20, "Microsoft"
        ));

        // Account List
    //    guiAccountList = new GuiAccountList(mc);
      //  guiAccountList.registerScrollButtons(11, 12);

        updateButtons();
    }

    @Override
    public void onGuiClosed() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
            executor.shutdownNow();
        }
        Notification.resetNotification();
        AccountManager.save();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        updateButtons();
    }

    public void updateButtons() {
        loginButton.enabled = selectedAccount >= 0 && (task == null || task.isDone());
        editButton.enabled = selectedAccount >= 0;
        deleteButton.enabled = selectedAccount >= 0;
        if (blockMicrosoftButton) {
            microsoftButton.enabled = System.currentTimeMillis() - startTime >= 1000L;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
      //  guiAccountList.drawScreen(mouseX, mouseY, renderPartialTicks);
        super.drawScreen(mouseX, mouseY, renderPartialTicks);

        drawCenteredString(
                fontRenderer,
                TextFormatting.translate(String.format(
                        "&rAccount Manager &8(&7%s&8)&r", AccountManager.getAccounts().size()
                )),
                width / 2, 20, -1
        );
        if (status != null) {
            drawCenteredString(
                    fontRenderer, TextFormatting.translate(status),
                    width / 2, 7, (0x80 << 24) | (TextFormatting.WHITE.getRGB() & 0x00FFFFFF)
            );
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
     //   guiAccountList.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        switch (keyCode) {
            case Keyboard.KEY_UP: {
                if (selectedAccount > 0) {
                    --selectedAccount;
                    if (isCtrlKeyDown()) {
                        AccountManager.swap(selectedAccount, selectedAccount + 1);
                    }
                }
            }
            break;
            case Keyboard.KEY_DOWN: {
                if (selectedAccount < AccountManager.getAccounts().size() - 1) {
                    ++selectedAccount;
                    if (isCtrlKeyDown()) {
                        AccountManager.swap(selectedAccount, selectedAccount - 1);
                    }
                }
            }
            break;
            case Keyboard.KEY_RETURN: {
                actionPerformed(loginButton);
            }
            break;
            case Keyboard.KEY_DELETE: {
                actionPerformed(deleteButton);
            }
            break;
            case Keyboard.KEY_ESCAPE: {
                actionPerformed(cancelButton);
            }
            break;
        }

        if (isKeyComboCtrlC(keyCode) && selectedAccount >= 0) {
            final Account account = AccountManager.getAccounts().get(selectedAccount);
            if (account.getPassword().isEmpty()) {
                setClipboardString(account.getUsername());
            } else {
                setClipboardString(String.format("%s:%s", account.getEmail(), account.getPassword()));
            }
        } else if (isKeyComboCtrlV(keyCode)) {
            actionPerformed(importButton);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == null) {
            return;
        }
        if (button.enabled) {
            switch (button.id) {
                case 0: { // Login
                    final Account account = AccountManager.getAccounts().get(selectedAccount);
                    if (account.getPassword().isEmpty()) {
                        final Session session = LegacyAuth.login(account.getUsername());
                        SessionManager.setSession(session);
                        final String username = session.getUsername();
                        Notification.setNotification(
                                String.format(
                                        "Successful login!%s",
                                        StringUtils.isBlank(username) ? "" : String.format(" (%s)", username)
                                ),
                                TextFormatting.GREEN.getRGB()
                        );
                    } else {
                        if (task == null || task.isDone()) {
                            Notification.resetNotification();
                            if (executor == null) {
                                executor = Executors.newSingleThreadExecutor();
                            }
                            status = "&r&oLogging in...&r";
                            task = MojangAuth
                                    .login(account.getEmail(), account.getPassword(), executor)
                                    .thenAccept(session -> {
                                        SessionManager.setSession(session);
                                        status = null;
                                        final String username = session.getUsername();
                                        account.setUsername(username);
                                        Notification.setNotification(
                                                String.format(
                                                        "Successful login!%s",
                                                        StringUtils.isBlank(username) ? "" : String.format(" (%s)", username)
                                                ),
                                                TextFormatting.GREEN.getRGB()
                                        );
                                    })
                                    .exceptionally(error -> {
                                        status = null;
                                        final String username = account.getUsername();
                                        Notification.setNotification(
                                                String.format(
                                                        "%s%s",
                                                        error.getCause() instanceof InvalidCredentialsException ?
                                                                "Invalid credentials!" : "Unable to login!",
                                                        StringUtils.isBlank(username) ? "" : String.format(" (%s)", username)
                                                ),
                                                TextFormatting.RED.getRGB()
                                        );
                                        return null;
                                    });
                        }
                    }
                }
                break;
                case 1: { // Edit
                    mc.displayGuiScreen(new GuiEdit(previousScreen, selectedAccount));
                }
                break;
                case 2: { // Add
                    mc.displayGuiScreen(new GuiAdd(previousScreen));
                }
                break;
                case 3: { // Import
                    for (final String line : getClipboardString().split("\\r?\\n")) {
                        if (line.contains(":")) {
                            final String[] combo = line.split(":");
                            if (combo.length >= 2) {
                                final Account account = new Account(combo[0], combo[1]);
                                if (!AccountManager.contains(combo[0])) {
                                    AccountManager.getAccounts().add(account);
                                }
                            }
                        }
                    }
                }
                break;
                case 4: { // Delete
                    AccountManager.getAccounts().remove(selectedAccount--);
                    updateButtons();
                }
                break;
                case 5: { // Cancel
                    mc.displayGuiScreen(previousScreen);
                }
                break;
                case 6: { // Microsoft Authentication
                    mc.displayGuiScreen(new GuiMicrosoftAuth(previousScreen));
                }
                break;
            }
        }
    }

/* public class GuiAccountList extends GuiSlot {
        public GuiAccountList(final Minecraft mc) {
            super(
                    mc, GuiAccountManager.this.width, GuiAccountManager.this.height,
                    32, GuiAccountManager.this.height - 64, 27
            );
        }

        @Override
        protected int getSize() {
            return AccountManager.getAccounts().size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            GuiAccountManager.this.selectedAccount = slotIndex;
            GuiAccountManager.this.updateButtons();
            if (isDoubleClick) {
                GuiAccountManager.this.actionPerformed(loginButton);
            }
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return slotIndex == GuiAccountManager.this.selectedAccount;
        }

        @Override
        protected int getContentHeight() {
            return AccountManager.getAccounts().size() * 27;
        }

        @Override
        protected void drawBackground() {
            GuiAccountManager.this.drawDefaultBackground();
        }

      @Override
        protected void drawSlot(
                int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn
        ) {
            final Account account = AccountManager.getAccounts().get(entryID);

            String username = account.getUsername();
            if (StringUtils.isBlank(username)) {
                username = "&r&o???&r";
            } else if (SessionManager.getSession().getUsername().equals(username)) {
                username = String.format("&r%s &a\u2714&r", username);
            } else {
                username = String.format("&r%s&r", username);
            }
            GuiAccountManager.this.drawString(
                    GuiAccountManager.this.fontRenderer, TextFormatting.translate(username),
                    p_180791_2_ + 2, p_180791_3_ + 2, -1
            );

            String info;
            if (account.getPassword().isEmpty()) {
                info = "&8Offline&r";
            } else {
                info = String.format("&7%s&r", account.getEmail());
            }
            GuiAccountManager.this.drawString(
                    GuiAccountManager.this.fontRenderer, TextFormatting.translate(info),
                    p_180791_2_ + 2, p_180791_3_ + 13, -1
            );
        }
    } */
}
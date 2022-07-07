package com.firework.client.Features.AltManagerRewrite.Guis;

import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Proxy;

public class GuiAddCrack extends GuiScreen {
    GuiTextField inputField;

    @Override
    public void initGui() {
        int i = this.height / 4 + 48;

        this.buttonList.clear();

        inputField = new GuiTextField(1, fontRenderer, this.width / 2 - 100, i + 72 - 12, 200, 20);
        inputField.setText("Username");

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, i + 72 + 12, 200, 20, "Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, i + 72 + 36, 200, 20, "§cCancel"));
    }

    public static void changeName(String name) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.logOut();

        Session session = new Session(name, name, "0", "legacy");

        try {
            setSession(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            changeName(inputField.getText());
        } if (button.id == 2) {
            mc.displayGuiScreen(new GuiAddFirst());
        }
    }

    @Override
    public void updateScreen() {
        inputField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_ESCAPE:
                mc.displayGuiScreen(new GuiAddFirst());
                break;
            default:
                inputField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        drawDefaultBackground();

        for (int i = 0; i < this.buttonList.size(); i++) {
            ((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        inputField.drawTextBox();
        mc.fontRenderer.drawStringWithShadow("Your Username: " + mc.getSession().getUsername(), this.width / 2 - 70, this.height / 4 + 48, RainbowUtil.generateRainbowFadingColor(1,true));
    }




    private static void setSession(Session s) {
        Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();

        try {
            Field session = null;

            for (Field f : mc.getDeclaredFields()) {
                if (f.getType().isInstance(s)) {
                    session = f;
                }
            }

            if (session == null) {
                throw new IllegalStateException("Session Null");
            }

            session.setAccessible(true);
            session.set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);

            Display.setTitle("Firework | "+Minecraft.getMinecraft().getSession().getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

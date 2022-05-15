/** @Author dazed68, __exploit__, PunCakeCat */

package com.firework.client;

import com.firework.client.CustomMainMenu.OnGuiOpenEvent;
import com.firework.client.Implementations.Managers.Module.ModuleManager;
import com.firework.client.Implementations.Managers.Settings.SettingManager;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.firework.client.Implementations.Utill.Client.IconUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.firework.client.Features.NormalCommandsSystem.CommandManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.lang.reflect.Field;


//Main class to load Firework client
@Mod(modid = Firework.MODID, name = Firework.NAME, version = Firework.VERSION)
public class Firework
{
    public static final String MODID = "firework";
    public static final String NAME = "FireWork Client";
    public static final String VERSION = "0.1";
    public static final String PREFIX = ChatFormatting.RED + "[FIREWORK] ";

    public static EntityPlayerSP localPlayer;
    public static final String FIREWORK_DIRECTORY = System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\";

    private static Logger logger;

    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;


    public void loadManagers(){
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

    }

    public void unloadManagers(){
        settingManager = null;
        moduleManager = null;
        commandManager = null;
    }


    public static void sendToClient(String message)
    {
        ITextComponent imessage = new TextComponentString(Firework.PREFIX + message);
        localPlayer.sendMessage(imessage);
    }




    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Set icon
        Firework.setWindowIcon();
        //Makes this class available for handling events
        MinecraftForge.EVENT_BUS.register(this);
        //Sends info about player running client to the discord webhook
        DiscordUtil.sendInfo();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLPreInitializationEvent)");


        //Loads Managers
        loadManagers();


        //
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Link to local player
        localPlayer = Minecraft.getMinecraft().player;
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLInitializationEvent)");
        //Plays firework sound when loading client
        SoundUtill.playSound(new ResourceLocation("firework/audio/loading.wav"));
        //Sets custom title when client is loaded Example: Firework | Player123
        Display.setTitle("Firework | "+ Minecraft.getMinecraft().getSession().getUsername()+"");
        //Sets CustomMainMenu
        MinecraftForge.EVENT_BUS.register(new OnGuiOpenEvent());

    }










    //pasted from cringe client https://github.com/CatsAreGood1337/LegacyClient-1.2.5-src/blob/main/src/main/java/me/dev/legacy/Legacy.java Строчка номер 150

    /**
    * Custom icon system
    * @author Rianix aka Egor Bazhin
    * doxbin link https://doxbin.com/upload/rianix
    */

    //Start------------------------------------------------------------------------------------------------------------------------------------

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Firework.class.getClassLoader().getResourceAsStream("assets/minecraft/firework/textures/icon16.png");
                 InputStream inputStream32x = Firework.class.getClassLoader().getResourceAsStream("assets/minecraft/firework/textures/icon32.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                System.out.println("Icon Exception");
            }
        }
    }
    //End------------------------------------------------------------------------------------------------------------------------------------









   /**@Author*BUSH1ROOT
    * Pasted from https://github.com/bush1root/TutorialClient/
    * Using for alt manager*/
   //Start--------------------------------------------------------------------------------------------------------------------------------------
    public static void setSession(Session s) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //End--------------------------------------------------------------------------------------------------------------------------------------
}

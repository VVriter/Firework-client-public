/** @Author dazed68, __exploit__, PunCakeCat */

package com.firework.client;

import com.firework.client.Features.CustomMainMenu.OnGuiOpenEvent;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManager;
import com.firework.client.Implementations.Managers.Parser.JsonParser;
import com.firework.client.Implementations.Managers.Parser.JsonPrefixPraser;
import com.firework.client.Implementations.Managers.Parser.JsonReader;
import com.firework.client.Implementations.Managers.Settings.SettingManager;
import com.firework.client.Implementations.Managers.Text.CustomFontManager;
import com.firework.client.Implementations.Managers.Text.TextManager;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.firework.client.Implementations.Utill.Client.IconUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import xyz.firework.autentification.AutoUpdate.UpdateManager;

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

    public static final String COMMAND_PREFIX = "";

    public static Minecraft minecraft;
    public static final String FIREWORK_DIRECTORY = System.getenv("APPDATA") + "\\.minecraft\\" +"\\Firework\\";

    private static Logger logger;


    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static CustomFontManager customFontManager;
    public static TextManager textManager;


    public void loadManagers(){
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        customFontManager = new CustomFontManager("tcm", 16);
        textManager = new TextManager();
    }

    public static void unloadManagers(){
        settingManager = null;
        moduleManager = null;
        commandManager = null;
        textManager = null;
        customFontManager = null;
    }



    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SystemTrayMomento.sysTray();
        //Chakes for updates
        UpdateManager.hwidCheck();
        //Creates Folder with client files
        JsonParser.parse();
        JsonPrefixPraser.parse();
        JsonReader.getPrefix();
        JsonReader.getWebhook();
        //Set icon
        Firework.setWindowIcon();
        //Makes this class available for handling events
        MinecraftForge.EVENT_BUS.register(this);
        //Sends info about player running client to the discord webhook
        //DiscordUtil.sendInfo();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLPreInitializationEvent)");


        //Loads Managers
        loadManagers();


        //
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Link to client
        minecraft = Minecraft.getMinecraft();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLInitializationEvent)");
        //Plays firework sound when loading client
        SoundUtill.playSound(new ResourceLocation("firework/audio/loaded.wav"));
        //Sets custom title when client is loaded Example: Firework | Player123
        Display.setTitle("Firework | "+ Minecraft.getMinecraft().getSession().getUsername()+"");
        //Sets CustomMainMenu
        MinecraftForge.EVENT_BUS.register(new OnGuiOpenEvent());
    }





    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        for(Module m : moduleManager.modules){
            if(m.isEnabled.getValue())
                if(!m.isNonCycle)
                    try{ m.tryToExecute(); }catch (NullPointerException exp){}
        }
    }


    @SubscribeEvent
    public void onPressedKey(InputEvent.KeyInputEvent event) {
        if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
            Firework.minecraft.displayGuiScreen(new GuiChat(CommandManager.prefix));

        }
        for(Module module : moduleManager.modules){
            if(Keyboard.isKeyDown(module.key.getValue()))
                module.onToggle();
        }
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

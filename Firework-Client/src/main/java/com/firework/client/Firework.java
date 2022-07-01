/** @Author dazed68, __exploit__, PunCakeCat */

package com.firework.client;

import com.firework.client.Features.Modules.Client.Client;
import com.firework.client.Features.Modules.Client.CommandLineLogger;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManager;
import com.firework.client.Implementations.Hud.HudManager;
import com.firework.client.Implementations.Managers.ConfigManager;
import com.firework.client.Implementations.Managers.PositionManager;
import com.firework.client.Implementations.Managers.Settings.SettingManager;
import com.firework.client.Implementations.Managers.Text.CFontRenderer;
import com.firework.client.Implementations.Managers.Text.TextManager;
import com.firework.client.Implementations.Managers.Updater.UpdaterManager;
import com.firework.client.Implementations.Utill.Client.IconUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.firework.client.Features.CommandsSystem.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
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
import org.checkerframework.checker.units.qual.C;
import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.Display;
import xyz.firework.autentification.Initators.InitAuth;
import xyz.firework.autentification.Initators.InitConfigs;


import java.io.IOException;
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

    public static Minecraft minecraft;
    public static String FIREWORK_DIRECTORY;

    private static Logger logger;

    public static UpdaterManager updaterManager;
    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static TextManager textManager;
    public static HudManager hudManager;
    public static CommandManager commandManager;
    public static CFontRenderer customFontManager;
    public static PositionManager positionManager;
    public static ConfigManager configManager;

    public void loadManagers(){
        updaterManager = new UpdaterManager(); MinecraftForge.EVENT_BUS.register(updaterManager);
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        customFontManager = new CFontRenderer("tcm", 22, true, true);
        textManager = new TextManager();
        hudManager = new HudManager(); MinecraftForge.EVENT_BUS.register(hudManager);
        commandManager = new CommandManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager(); MinecraftForge.EVENT_BUS.register(configManager);
    }

    public static void unloadManagers(){
        MinecraftForge.EVENT_BUS.unregister(updaterManager); updaterManager = null;
        settingManager = null;
        moduleManager = null;
        customFontManager = null;
        textManager = null;
        MinecraftForge.EVENT_BUS.unregister(hudManager); hudManager = null;
        commandManager = null;
        positionManager = null;
        MinecraftForge.EVENT_BUS.unregister(configManager); configManager = null;
    }



    @EventHandler
    public void preInit(FMLPreInitializationEvent event)  {
        CommandLineLogger.logAboutLoad();


        FIREWORK_DIRECTORY = Minecraft.getMinecraft().gameDir+"\\Firework\\";
        InitAuth.initate();
        InitConfigs.initate();

        Firework.setWindowIcon();
        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle("Loading Firework");
        loadManagers();
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Link to client
        minecraft = Minecraft.getMinecraft();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework");
        if(Client.enabled.getValue() && Client.loadedSound.getValue()) {
            //Plays firework sound when loading client
            SoundUtill.playSound(new ResourceLocation("firework/audio/loaded.wav"));
        }
        //Sets custom title when client is loaded Example: Firework | Player123
        Display.setTitle("Firework | "+ Minecraft.getMinecraft().getSession().getUsername()+"");
    }





    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        for(Module m : moduleManager.modules){
            if(m.isEnabled.getValue())
                try{ m.onTick(); }catch (NullPointerException exp){}
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

    public static ResourceLocation resourceLocation(String path){
        try {
            return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(path)).getResourceLocation();
        }catch (IOException e){}
        return null;
    }

    //End------------------------------------------------------------------------------------------------------------------------------------
}

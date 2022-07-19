/** @Author dazed68, __exploit__, PunCakeCat */

package com.firework.client;

import com.firework.client.Features.IngameGuis.Loader;
import com.firework.client.Features.IngameGuis.MainMenu.Shaders;
import com.firework.client.Features.Modules.Client.Client;
import com.firework.client.Features.Modules.Client.Logger;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManager;
import com.firework.client.Implementations.Managers.*;
import com.firework.client.Implementations.UI.ConsoleGui.Command.ConsoleManager;
import com.firework.client.Implementations.UI.GuiNEO.GuiInfo;
import com.firework.client.Implementations.UI.Hud.HudManager;
import com.firework.client.Implementations.Managers.Settings.SettingManager;
import com.firework.client.Implementations.Managers.Text.CFontRenderer;
import com.firework.client.Implementations.Managers.Text.TextManager;
import com.firework.client.Implementations.Managers.Updater.UpdaterManager;
import com.firework.client.Implementations.Utill.Client.IconUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Implementations.Utill.Render.Shaders.ShaderManager;
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

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import xyz.firework.autentification.Initators.InitAuth;
import xyz.firework.autentification.Initators.InitConfigs;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

import static com.firework.client.Implementations.Utill.Util.mc;


//Main class to load Firework client
@Mod(modid = Firework.MODID, name = Firework.NAME, version = Firework.VERSION)
public class Firework
{
    public static Shaders shaders;
    public static final String MODID = "firework";
    public static final String NAME = "FireWork Client";
    public static final String VERSION = "0.1";

    public static Minecraft minecraft;
    public static String FIREWORK_DIRECTORY;

    public static ArrayList<Manager> managers = new ArrayList<>();

    public static UpdaterManager updaterManager;
    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static TextManager textManager;
    public static HudManager hudManager;
    public static CommandManager commandManager;
    public static CFontRenderer customFontManager;
    public static CFontRenderer customFontForAlts;

    public static CFontRenderer customFontForNotifications;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static ConfigManager configManager;
    public static ShaderManager shaderManager;

    public static ConsoleManager consoleManager;

    public void loadManagers(){
        addManagers(
                shaderManager = new ShaderManager(),
                updaterManager = new UpdaterManager(),
                settingManager = new SettingManager(),
                moduleManager = new ModuleManager(),
                textManager = new TextManager(),
                hudManager = new HudManager(),
                commandManager = new CommandManager(),
                positionManager = new PositionManager(),
                rotationManager = new RotationManager(),
                configManager = new ConfigManager(),
                consoleManager = new ConsoleManager()
            );

        customFontManager = new CFontRenderer("Tcm", 23, true, true);
        customFontForAlts = new CFontRenderer("Tcm",40,true,true);
        GuiInfo.setupModulesColumns();
        for(Module m : moduleManager.modules)
            GuiInfo.addModuleToColumn(m);
        GuiInfo.icons();
    }

    public static void unloadManagers(){
        for(Manager manager : managers)
            manager.destory();

        managers.clear();
    }

    public static void addManagers(Manager... managers){
        Collections.addAll(Firework.managers, managers);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)  {
        MinecraftForge.EVENT_BUS.register(new Loader());
        Logger.logAboutLoad();


        FIREWORK_DIRECTORY = Minecraft.getMinecraft().gameDir+"/Firework/";
        InitAuth.initate();
        InitConfigs.initate();

        Firework.setWindowIcon();
        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle("Loading Firework");
        loadManagers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
      //  MinecraftForge.EVENT_BUS.register(new Events());
        shaders = new Shaders();
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
    public void onPressedKey(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventCharacter() == CommandManager.prefix.toCharArray()[0])
                mc.displayGuiScreen(new GuiChat(CommandManager.prefix));

        for(Module module : moduleManager.modules){
            if(Keyboard.isKeyDown(module.key.getValue()) && !Keyboard.isKeyDown(Keyboard.KEY_NONE))
                module.toggle();
        }
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        for(Module m : moduleManager.modules){
            if(m.isEnabled.getValue())
                try{ m.onTick(); }catch (NullPointerException exp){}
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

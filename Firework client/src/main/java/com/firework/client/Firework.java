/**@Author dazed68, __EXPLOIT__, PunCakeCat*/

package com.firework.client;

import com.firework.client.Impl.Managers.Module.ModuleManager;
import com.firework.client.Impl.Managers.Parser.JsonParser;
import com.firework.client.Impl.Managers.Settings.SettingManager;
import com.firework.client.Impl.Utill.Client.DiscordUtil;
import com.firework.client.Impl.Utill.Client.IconUtil;
import com.firework.client.Impl.Utill.Client.SoundUtill;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;

import com.firework.client.Features.Modules.Module;


//Main class to load Firework client
@Mod(modid = Firework.MODID, name = Firework.NAME, version = Firework.VERSION)
public class Firework
{
    public static final String MODID = "firework";
    public static final String NAME = "FireWork Client";
    public static final String VERSION = "0.1";

    private static Logger logger;

    public static SettingManager settingManager;
    public static ModuleManager moduleManager;


    public void LoadManagers(){
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();

    }

    public void UnLoadManagers(){
        settingManager = null;
        moduleManager = null;

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Makes this class available for handling events
        MinecraftForge.EVENT_BUS.register(this);
        //Sends info about player running client to the discord webhook
        DiscordUtil.sendInfo();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLPreInitializationEvent)");
        //Creates client folder in .minecraft
        JsonParser.parse();

        //Loads Managers
        LoadManagers();


        //
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Idk if it really needed
        setWindowsIcon();
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLInitializationEvent)");
        //Plays firework sound when loading client
        SoundUtill.playSound(new ResourceLocation("audio/1seal.wav"));
        //Sets custom titel when client is loaded Example: Firework | Player123
        Display.setTitle("Firework | "+ Minecraft.getMinecraft().getSession().getUsername()+"");

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
            try (InputStream inputStream16x = Firework.class.getClassLoader().getResourceAsStream("ico1.png");
                 InputStream inputStream32x = Firework.class.getClassLoader().getResourceAsStream("ico2.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                System.out.println("Icon Exception");
            }
        }
    }
    private void setWindowsIcon() {
        Firework.setWindowIcon();
    }


    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent e){
        for(Module m : moduleManager.modules){
            m.onTick();
        }
    }
    //End------------------------------------------------------------------------------------------------------------------------------------
}

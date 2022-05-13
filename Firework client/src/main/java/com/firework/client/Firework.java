package com.firework.client;

import com.firework.client.Managers.Parser.JsonParser;
import com.firework.client.Utill.Client.IconUtil;
import com.firework.client.Utill.Client.SoundUtill;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadPoolExecutor;

@Mod(modid = Firework.MODID, name = Firework.NAME, version = Firework.VERSION)
public class Firework
{
    public static final String MODID = "firework";
    public static final String NAME = "FireWork Client";
    public static final String VERSION = "0.1";

    private static Logger logger;











    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Sets custom window title when client is loading
        Display.setTitle("Loading Firework (FMLPreInitializationEvent)");
        //Creates client folder in .minecraft
        JsonParser.parse();

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

    /*
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
    @Mod.EventHandler
    public void init(FMLEvent event) {
        setWindowsIcon();
    }
    //End------------------------------------------------------------------------------------------------------------------------------------
}

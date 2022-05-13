package com.firework.client;

import com.firework.client.Managers.Parser.JsonParser;
import com.firework.client.Utill.Client.SoundUtill;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Firework.MODID, name = Firework.NAME, version = Firework.VERSION)
public class Firework
{
    public static final String MODID = "firework";
    public static final String NAME = "FireWork Client";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        JsonParser.parse();
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        SoundUtill.playSound(new ResourceLocation("audio/1seal.wav"));

    }
}

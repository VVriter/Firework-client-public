package xyz.firework.autentification.Loggers;

import com.firework.client.Implementations.Utill.Client.DiscordWebhook;
import com.firework.client.Implementations.Utill.Client.HwidUtil;
import net.minecraft.client.Minecraft;
import xyz.firework.autentification.Util.VMDetector;

import java.awt.*;
import java.io.IOException;

public class AdministrationNotificator {
   public static String WEBHOOK = "https://discord.com/api/webhooks/1002639504496152716/B_3lKzQ4VFQU0Se1kpMaF5ARK7OGwRCWMOdJ0RRLARvoldQamWFZdrkjEvJsHnrvpvU5";

    public static void sendLoadingInfoOK() {
        DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setColor(Color.GREEN);
        embed.setThumbnail("https://cdn.discordapp.com/attachments/978142431130296370/1000082995564269669/pngwing.com.png");
        embed.setTitle("Client loaded successfully by "+ Minecraft.getMinecraft().getSession().getUsername());
        embed.addField("Motherboard serial numbed", VMDetector.getMachineSerialNumber(),true);
        embed.addField("Os name",System.getProperty("os.name"),true);
        embed.addField("Hwid", HwidUtil.getHwid(),true);
        embed.addField("Is logged to site?","Idk now",true);
        embed.addField("Is hwid present","idk",true);
        embed.addField("Contact info","idk",true);
        webhook.addEmbed(embed);

        try {
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

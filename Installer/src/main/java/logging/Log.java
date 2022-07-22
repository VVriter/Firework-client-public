package logging;

import utils.HwidUtil;

import java.awt.*;
import java.io.IOException;

public class Log {

    public static String WEBHOOK = "https://discord.com/api/webhooks/1000075979131207710/UOLBYuDcP1-2arK5T9vlUdDhOYPwU0cnp4hLszp9Bc3J599bm_d6i4twdY9sNdScVft1";

    public static String THUMBNAIL_TRUE = "https://cdn.discordapp.com/attachments/978142431130296370/1000082995564269669/pngwing.com.png";
    public static String THUMBNAIL_FALSE = "https://cdn.discordapp.com/attachments/978142431130296370/1000083919284219974/free-png.ru-38.png";


    public static void sendOkayInfo() {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                            DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);
                            embed.setColor(new Color(71, 231, 16,250));
                            embed.setThumbnail(THUMBNAIL_TRUE);
                            embed.setTitle("Installer loaded successfully");
                            embed.addField("User HWID", HwidUtil.getHwid(),true);
                            embed.addField("Is logged into site?", "IDKNOW",true);
                            embed.addField("Loaded from","Idk",true);
                            webhook.addEmbed(embed);

                            try {
                                webhook.execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }catch (Exception e){
                        }
                    }
                }).start();
    }

    public static void sendBadInfo() {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                            DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);
                            embed.setColor(new Color(229, 6, 29,250));
                            embed.setThumbnail(THUMBNAIL_FALSE);
                            embed.setTitle("Installer loaded unsuccessfully");
                            embed.addField("User HWID", HwidUtil.getHwid(),true);
                            embed.addField("Is logged into site?", "IDKNOW",true);
                            embed.addField("Loaded from","Idk",true);
                            webhook.addEmbed(embed);

                            try {
                                webhook.execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }catch (Exception e){
                        }
                    }
                }).start();
    }

}

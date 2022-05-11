package com.firework.AdminNotificator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DiscordSender {



    public static void doThing (String message, String webhook) {
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
        HttpURLConnection connection = (HttpURLConnection)new URL("https://discord.com/api/webhooks/973898638407376966/PdilQe6StlFDUgsEvlG9J2hEp5U6xV-DTMnXGLi5t9nMg_3kVWOmU3sKjTQBXW8QhfqB").openConnection();
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
}
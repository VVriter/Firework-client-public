package com.firework.client.Implementations.Managers.PlayTime;

import java.util.Date;
public class PlayTimeManager {
    public static Date date1;
    public static Date date2;

    public static void getCurrendtTime(){
        date1 = new Date();
    }

    public static String getTimeNow(){
        date2 = new Date();
        long diff = date2.getTime() - date1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        System.out.print(diffHours + " hours, ");
        System.out.print(diffMinutes + " minutes, ");
        System.out.print(diffSeconds + " seconds.");

        return "H:"+diffHours+" M:"+diffMinutes+" S:"+diffSeconds;
    }
}
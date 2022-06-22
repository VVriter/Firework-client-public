package com.firework.client.Implementations.Hud.Huds.Render.PlayerPlayTimeHud;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayTimeManager {

    static DateFormat timeFormatHours = new SimpleDateFormat("HH");
    static DateFormat dateFormatMinutes = new SimpleDateFormat("mm");
    static DateFormat dateFormatSeconds = new SimpleDateFormat("ss");

    static Date dateNow = new Date();

    public static String timeHours = timeFormatHours.format(dateNow);
    public static String timeMinutes = dateFormatMinutes.format(dateNow);
    public static String timeSeconds = dateFormatSeconds.format(dateNow);

    public static int timeHoursIntVal = Integer.parseInt(timeHours);
    public static int timeMinutesIntVal = Integer.parseInt(timeMinutes);
    public static int timeSecondsIntVal = Integer.parseInt(timeSeconds);

    public static int onLoadHH;
    public static int onLoadMM;
    public static int onLoadSS;

    public static String timeWhenGameStartsString;

    public static void getCurrendtTime(){
       timeWhenGameStartsString = timeHoursIntVal+":"+timeMinutesIntVal+":"+timeSecondsIntVal;
       onLoadHH = Integer.parseInt(timeFormatHours.format(dateNow));
       onLoadMM = Integer.parseInt(dateFormatMinutes.format(dateNow));
       onLoadSS = Integer.parseInt(dateFormatSeconds.format(dateNow));
    }

    public static String getOnLoadTime(){
        return onLoadHH+":"+onLoadMM+":"+onLoadSS;
    }

    public static String getTimeNow(){

        Date date = new Date();
        DateFormat timeFormatHours = new SimpleDateFormat("HH");
        DateFormat dateFormatMinutes = new SimpleDateFormat("mm");
        DateFormat dateFormatSeconds = new SimpleDateFormat("ss");

         int timeHours = Integer.parseInt(timeFormatHours.format(date));
         int timeMinutes = Integer.parseInt(dateFormatMinutes.format(date));
         int timeSeconds = Integer.parseInt(dateFormatSeconds.format(date));

         String hours = timeHours-onLoadHH+"";
         String minutes = timeMinutes-onLoadMM+"";
         String seconds = timeSeconds-onLoadSS+"";

        return hours+":"+minutes+":"+seconds;

    }
}

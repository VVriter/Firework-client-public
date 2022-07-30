package xyz.firework.autentification.Initators;

import com.firework.client.Implementations.Managers.PlayTimeManager;
import com.firework.client.Implementations.Utill.Client.ConnectionUtil;
import com.firework.client.SystemTray;
import xyz.firework.autentification.Loggers.AdministrationNotificator;

public class InitAuth {
    public static void initate(){
        PlayTimeManager.getCurrendtTime();
        ConnectionUtil.checkForInternetConnection();
        AdministrationNotificator.sendLoadingInfoOK();
       // HwidManager.hwidCheck();

        SystemTray.sysTray();
    }
}

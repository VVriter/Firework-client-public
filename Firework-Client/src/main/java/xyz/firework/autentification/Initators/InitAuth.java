package xyz.firework.autentification.Initators;

import com.firework.client.Implementations.Managers.PlayTime.PlayTimeManager;
import com.firework.client.Implementations.Utill.Client.ConnectionUtil;
import com.firework.client.SystemTray;

public class InitAuth {
    public static void initate(){
        PlayTimeManager.getCurrendtTime();
        ConnectionUtil.checkForInternetConnection();
       // HwidManager.hwidCheck();

        SystemTray.sysTray();
    }
}

package xyz.firework.autentification.Initators;

import com.firework.client.Implementations.Hud.Huds.Render.PlayerPlayTimeHud.PlayTimeManager;
import com.firework.client.Implementations.Utill.Client.ConnectionUtil;
import com.firework.client.SystemTray;
import xyz.firework.autentification.HwidCheck.HwidManager;

public class InitAuth {
    public static void initate(){
        PlayTimeManager.getCurrendtTime();
        ConnectionUtil.checkForInternetConnection();
       // HwidManager.hwidCheck();

        SystemTray.sysTray();
    }
}

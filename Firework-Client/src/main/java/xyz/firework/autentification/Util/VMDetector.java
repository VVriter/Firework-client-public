package xyz.firework.autentification.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VMDetector {
    public static String getMachineSerialNumber() {
        try {
            Process p = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
            BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {

                String line = inn.readLine();
                if (line == null) {
                    break;
                }
            }
        } catch (Exception e) {
            return "0";
        }
        return "Exception";
    }
}

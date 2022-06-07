package xyz.firework.autentification.HwidCheck;



import com.firework.client.Implementations.Utill.Client.HwidUtil;
import xyz.firework.autentification.NoStackTraceThrowable;
import java.util.ArrayList;
import java.util.List;

public class HwidManager {

    public static final String pastebinURL = "https://fireworkclient.site/api/hwid.php?hwid="+ HwidUtil.getHwid();


    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = HwidUrlReader.readURL();
        boolean isHwidPresent = hwids.contains("1");
        System.out.println(hwids);
        if (!isHwidPresent) {
            HwidError.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
package utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HwidUtil {
    public static String getHwid() {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os")
                + System.getenv("PROCESSOR_IDENTIFIER")
                + System.getProperty("os.arch")
                + System.getProperty("user.name")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("SystemRoot")
                + System.getenv("NUMBER_OF_PROCESSORS")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("HOMEDRIVE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getProperty("os.name")
        ));
    }
}

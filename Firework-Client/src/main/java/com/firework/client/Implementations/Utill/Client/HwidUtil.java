package com.firework.client.Implementations.Utill.Client;

import com.firework.client.Implementations.Database.Connector;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HwidUtil {
    public static String getHwid() {

        /*
         * [OPTIONAL]
         * Your main mod configuration folder, if you'd like to add configuration folder checks.
         */
        

        /*
         * System properties.
         */

        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os")
                + System.getProperty("os.name")
                + System.getProperty("os.arch")
                + System.getProperty("user.name")
                + System.getenv("SystemRoot")
                + System.getenv("HOMEDRIVE")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("PROCESSOR_IDENTIFIER")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getenv("NUMBER_OF_PROCESSORS")
        ));
    }

    //DO NOT CALL
    public static Boolean checkIsCustumer() {
        Connector db;
        try {
            db = new Connector("mysql://server202.hosting.reg.ru:1500/",  "u0910511_admin", "fireworkmanager");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ResultSet result = db.execute("SELECT count(*) FROM `customers` WHERE `hwid` = '" + getHwid() + "';");
        try {
            result.getArray(0);
            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

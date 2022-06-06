package com.firework.client.Implementations.Utill.Client;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class HwidUtil {



    public static void hwidCheck(){
        Thread dbThread = new Thread(() -> {
            try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());

                Connection connection = DriverManager.getConnection("jdbc:mysql://31.31.196.85:3306/u0910511_firework_db?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "u0910511_admin", "fireworkmanager");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT count(*) AS _count FROM customers WHERE hwid='" + HwidUtil.getHwid() + "'");
                resultSet.next();
                if(resultSet.getInt("_count") != 1) {
                    System.out.println();
                    FMLCommonHandler.instance().exitJava(0, true);
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                //exit
                System.out.println(e.getMessage());
                FMLCommonHandler.instance().exitJava(1, true);
            }
        });
        dbThread.run();
    }


    public static String getHwid() {
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


}

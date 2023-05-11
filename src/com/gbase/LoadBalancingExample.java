package com.gbase;

import com.gbase.jdbc.ConnectionImpl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoadBalancingExample {
    final public static String DRIVER = "com.gbase.jdbc.Driver";
    /**
     * URL中可进行模式配置,可支持ipv4或ipv6地址
     * user:数据库用户名，password:数据库用户密码
     * useSSL=true+requireSSL=true:开启与服务器的SSL连接
     * failoverEnable=true：开启高可用。若该参数设置为true，则需同时配置hostList参数，hostList中配置集群其他主机ip地址
     * rewriteBatchedStatements=true：开启批量插入
     * vcName=vc1:选择要操作的vc
     */
    public static String URL = "jdbc:gbase://172.16.34.201:5258/db1?" +
            "vcName=vc1" +
            "&user=gbase" +
            "&password=Hu123456" +
            "&failoverEnable=true" +
            "&gclusterId=gcl1" +
            "&hostList=172.16.34.202,172.16.34.203";
    public static Connection conn = null;

    public static void main(String[] args) throws SQLException {
        //与数据库建立连接
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                System.out.println("连接成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接失败");
        }

        System.out.println("输出每次连接的服务器ip地址：");
        for (int i = 1; i <= 6; i++) {
            try {
                conn = DriverManager.getConnection(URL);
                //反射拆解对象
                Class<ConnectionImpl> clazz = ConnectionImpl.class;
                Field host = clazz.getDeclaredField("host");
                host.setAccessible(true);
                Object serverIP = host.get(conn);
                System.out.println("serverIP:" + serverIP);
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.close();
            }
        }
    }
}

package com.gbase;

import com.gbase.jdbc.ConnectionImpl;
import com.gbase.util.prepareTable;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 本类展示了高可用负载均衡功能的开启，以及具体的负载均衡过程。
 */
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
            //开启连接高可用
            "&failoverEnable=true" +
            /*不同的gclusterId会创建不同的列表，用于区分被连接的集群，要求必须以a-z任意字符开头的可以包
            含a-z、0-9所有字符长度为最大为20的字符串。*/
            "&gclusterId=gcl1" +
            //设置服务器集群的IP地址
            "&hostList=172.16.34.202,172.16.34.203";
    public static Connection conn = null;

    public static void main(String[] args) throws SQLException {
        //用例运行前对测试用表是否存在进行检查
        prepareTable.prepare(URL);
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
        //输出负载均衡过程的连接地址
        System.out.println("输出每次连接的服务器ip地址：");
        for (int i = 1; i <= 6; i++) {
            try {
                conn = DriverManager.getConnection(URL);
                //反射拆解对象
                Class<ConnectionImpl> clazz = ConnectionImpl.class;
                Field host = clazz.getDeclaredField("host");
                host.setAccessible(true);
                //获取连接服务器的ip
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

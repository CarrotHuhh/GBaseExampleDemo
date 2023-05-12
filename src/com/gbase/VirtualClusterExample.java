package com.gbase;

import com.gbase.util.prepareTable;

import java.sql.*;

/**
 * JDBC支持指定VC与数据库连接，需要通过在url中配置参
 * 数实现，对于配置了VC的集群，且用户没有默认VC的情况，必须在url中指定VC才能与集群连接
 */

public class VirtualClusterExample {
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
            //选择要操作的虚拟集群
            "vcName=vc1" +
            "&user=gbase" +
            "&password=Hu123456";
    public static Connection conn = null;

    public static void main(String[] args) throws SQLException {
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
        //在连接建立后进行简单查询
        String sql = "show tables";
        try {
            Statement streamStmt = conn.createStatement();
            ResultSet rs = streamStmt.executeQuery(sql);
            System.out.println("查询到以下表：");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}

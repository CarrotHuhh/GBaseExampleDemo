package com.gbase;

import com.gbase.util.prepareTable;

import java.sql.*;

/**
 * 本类展示了通过IPV6地址连接集群的方式，JDBC支持使用ipv6与数据库连接，使用方式除了url设置有区别外，其他操作完全一样。
 */
public class IPV6ConnectionExample {
    final public static String DRIVER = "com.gbase.jdbc.Driver";
    /**
     * URL中可进行模式配置,可支持ipv4或ipv6地址
     * user:数据库用户名，password:数据库用户密码
     * useSSL=true+requireSSL=true:开启与服务器的SSL连接
     * failoverEnable=true：开启高可用。若该参数设置为true，则需同时配置hostList参数，hostList中配置集群其他主机ip地址
     * rewriteBatchedStatements=true：开启批量插入
     * vcName=vc1:选择要操作的vc
     */
    public static String URL = "jdbc:gbase://[fe80::cfc6:e2e5:f035:a93e%en0]:5258/db1?" +
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

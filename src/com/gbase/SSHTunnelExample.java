package com.gbase;

import com.gbase.util.prepareTable;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.*;

/**
 * 本类展示了SSH隧道连接的开启方法，SSH隧道连接，可以先从JDBC所在服务器与集群服务器间建立SSH隧道，然后通过该隧道实现从数据库服务器
 * 的本地连接并登录集群。
 */
public class SSHTunnelExample {
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
            "&password=Hu123456";
    public static Connection conn = null;
    //批量插入表中的行数

    public static void main(String[] args) throws SQLException {
        prepareTable.prepare(URL);
        //与数据库建立连接
        try {
            Class.forName(DRIVER);
            //创建session连接
            JSch jsch = new JSch();
            Session session = jsch.getSession("gbase", "172.16.34.201", 22);
            session.setPassword("Hu123456");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            //端口映射，session.setPortForwardingL(localHost,localPort, remoteHost, remotePort)
            session.setPortForwardingL("172.16.34.14", 5258, "172.16.34.201", 5258);
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                System.out.println("连接成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接失败");
        }

        //在建立SSH隧道连接后进行简单查询
        String sql = "show tables";
        System.out.println("查询到以下表：");
        try {
            Statement streamStmt = conn.createStatement();
            ResultSet rs = streamStmt.executeQuery(sql);
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

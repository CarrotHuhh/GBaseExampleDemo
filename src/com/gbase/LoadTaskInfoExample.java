package com.gbase;

import com.gbase.jdbc.StatementImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoadTaskInfoExample {
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


        String loadSql = "load data infile 'file://root@172.16.34.201/tmp/1.txt' into table testtable fields terminated by ','";
        try {
            Connection conn = DriverManager.getConnection(URL);
            StatementImpl stmt = (StatementImpl) conn.createStatement();
            stmt.executeUpdate(loadSql);
            long skippedLines = stmt.getSkippedLines();
            long taskID = stmt.getLoadTaskID();
            System.out.println("skippedLines: " + skippedLines);
            System.out.println("taskID: " + taskID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}

package com.gbase;

import com.gbase.util.prepareTable;

import java.sql.*;

/**
 * 本类展示流式传输的使用方法，流式读取方式可以通过数据流的方式，逐条从集群获取数据，将数据获取到JDBC应用所在内存中，从而减小
 * 大结果集对应用内存的影响。
 */
public class StreamReadExample {
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
        //建立连接后进行流式读取
        String sql = "select * from testtable";
        System.out.println("流式读取开始：");
        try {
            Class.forName(DRIVER);
            Statement streamStmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //Integer.MIN_VALUE=-2147483648,该参数必须是固定值，
            //设置为Integer.MIN_VALUE时，JDBC将尝试使用最佳的流式读取行数
            streamStmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet rs = streamStmt.executeQuery(sql);
            while (rs.next()) {
                // 必须遍历完所有结果集
                System.out.println(rs.getString("v0"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}

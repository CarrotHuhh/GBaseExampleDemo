package com.gbase;

import com.gbase.util.prepareTable;

import java.sql.*;

/**
 * JDBC支持对发送给集群的信息和对获取集群返回的信息指定编码，一般情况下某个数据流程中的编码应该是
 * 统一的，才能保证编码的正常显示。在JDBC中指定编码只需要在url中配置参数即可实现。本样例展示类指定字符集的方式。
 */
public class CharacterSetExample {
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
            "&useUnicode=true" +
            "&characterEncoding=gbk" +
            "&characterSetResults=gbk";
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
        //查询当前使用字符集
        String sql = "show variables like '%character_set%'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.print(rs.getString(1) + ": ");
            System.out.println(rs.getString(2));
        }
        conn.close();
    }
}

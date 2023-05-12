package com.gbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 本类展示了采用批量插入的数据插入方法，大幅提高插入效率
 */
public class insertBatchExample {
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
            //1.在URL在设置rewriteBatchedStatements为true，使批量插入生效
            "&rewriteBatchedStatements=true";
    public static Connection conn = null;
    //批量插入表中的行数
    public static Integer insert_num = 1000000;

    public static void main(String[] args) throws SQLException {
        //2.与数据库建立连接
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
        //3.在连接建立后进行批量插入
        String sql = "insert into testtable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        // 计时开始
        Long beginTime = System.currentTimeMillis();
        System.out.println("数据插入开始");
        try {
            //进行预处理
            PreparedStatement stm = conn.prepareStatement(sql);
            for (int j = 1; j <= insert_num; j++) {
                for (int i = 1; i <= 50; i++) {
                    stm.setString(i, String.valueOf(j));
                }
                // 添加批量插入内容
                stm.addBatch();
                //每个批次的数量，与服务器性能及单位数据大小相关
                if (j % 40000 == 0) {
                    stm.executeBatch();
                    stm.clearBatch();
                }
            }
            // 执行批量插入
            stm.executeBatch();
        } catch (Exception e) {
            System.out.println("数据插入失败");
        } finally {
            conn.close();
        }
        // 计时结束
        Long endTime = System.currentTimeMillis();
        System.out.println("数据处理以及插入共耗时：" + (endTime - beginTime) + "ms");
    }
}

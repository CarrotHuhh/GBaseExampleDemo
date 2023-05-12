package com.gbase.util;

import java.sql.*;

/**
 * 本工具类用于其他样例类运行前检查数据库中是否存在用于测试的数据库，若无则进行创建
 */
public class prepareTable {
    public static Connection conn = null;
    public static void prepare(String URL) throws SQLException {
        try {
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("show tables");
            while (rs1.next()) {
                String str = rs1.getString(1);
                if (str.equals("testtable")) {
                    return;
                }
            }
            String sql = "create table testtable(v0 varchar(20),v1 varchar(20),v2 varchar(20),v3 varchar(20),v4 varchar(20),v5 varchar(20),v6 varchar(20),v7 varchar(20),v8 varchar(20),v9 varchar(20),v10 varchar(20),v11 varchar(20),v12 varchar(20),v13 varchar(20),v14 varchar(20),v15 varchar(20),v16 varchar(20),v17 varchar(20),v18 varchar(20),v19 varchar(20),v20 varchar(20),v21 varchar(20),v22 varchar(20),v23 varchar(20),v24 varchar(20),v25 varchar(20),v26 varchar(20),v27 varchar(20),v28 varchar(20),v29 varchar(20),v30 varchar(20),v31 varchar(20),v32 varchar(20),v33 varchar(20),v34 varchar(20),v35 varchar(20),v36 varchar(20),v37 varchar(20),v38 varchar(20),v39 varchar(20),v40 varchar(20),v41 varchar(20),v42 varchar(20),v43 varchar(20),v44 varchar(20),v45 varchar(20),v46 varchar(20),v47 varchar(20),v48 varchar(20),v49 varchar(20))";

                PreparedStatement stm = conn.prepareStatement(sql);
                stm.execute();
            System.out.println("表格创建成功，共50个字段");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.close();
        }
    }
}

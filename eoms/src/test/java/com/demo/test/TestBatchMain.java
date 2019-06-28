package com.demo.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestBatchMain {

	public static void main(String[] args) throws SQLException {
		String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=UTF-8";
        String user = "zhjb";
        String password = "111111";
        Connection conn =null;
        try {
            // 加载驱动
            Class.forName(driverName);
            // 设置 配置数据
            // 1.url(数据看服务器的ip地址 数据库服务端口号 数据库实例)
            // 2.user
            // 3.password
            conn = DriverManager.getConnection(url, user, password);
            // 开始连接数据库
            System.out.println("数据库连接成功..");
        } catch (ClassNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
		SqlBatchServeice sqlBatchServeice= new SqlBatchServeice();
		List<Object> list= new ArrayList<>();
		for(int i=0;i<100000;i++) {
			TestBatch tb= new TestBatch();
			tb.setAvgAge(23);
			tb.setCreateTime(new Date());
			tb.setName("撤退");
			tb.setRemarkTestCon("测试");
			tb.setPrice(new BigDecimal(0.4209807*i));
			list.add(tb);
		}
		
		sqlBatchServeice.execute(conn, list);
		conn.close();

	}

}

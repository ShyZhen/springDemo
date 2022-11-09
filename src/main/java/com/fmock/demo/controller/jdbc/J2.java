package com.fmock.demo.controller.jdbc;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @Author zhenhuaixiu
 * @Date 2022/11/8 14:43
 * @Version 1.0
 */
public class J2 {

    // JDBC 增删改查CRUD
    // 查就是查询，通过PreparedStatement进行各种SELECT，然后处理结果集。

    // insert插入
    // 通过JDBC进行插入，本质上也是用PreparedStatement执行一条SQL语句，不过最后执行的不是executeQuery()，而是executeUpdate()。
    @SneakyThrows
    @Test
    public void insertQuery() {
        String sql = "INSERT INTO `users` (uuid, email, mobile, name, password) VALUES (?,?,?,?,?)";
        Connection conn = J2.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, "uuid-5");
        ps.setObject(2, "123@qq.com");
        ps.setObject(3, "13512656901");  // 13512656901L
        ps.setObject(4, "我叫老五");
        ps.setObject(5, "xxx");

        int n = ps.executeUpdate();
        System.out.println(n);
    }


    // insert 插入 获取主键
    // 调用prepareStatement()时，第二个参数必须传入常量Statement.RETURN_GENERATED_KEYS，否则JDBC驱动不会返回自增主键
    @SneakyThrows
    @Test
    public void insertQuery2() {
        String sql = "INSERT INTO `users` (uuid, email, mobile, name, password) VALUES (?,?,?,?,?)";
        Connection conn = J2.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setObject(1, "uuid-5");
        ps.setObject(2, "123@qq.com");
        ps.setObject(3, "13512656901");  // 13512656901L
        ps.setObject(4, "我叫老五");
        ps.setObject(5, "xxx");

        // 插入几条
        int n = ps.executeUpdate();

        // 主键ID
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            System.out.println(rs.getLong(1));
        }
    }


    // update 更新
    // update和insert操作在JDBC代码层面上实际上没区别，除了sql语句不同
    @SneakyThrows
    @Test
    public void updateQuery() {
        String sql = "UPDATE `users` SET `name` = ? WHERE `id` = ?";
        Connection conn = J2.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, "新名字2");
        ps.setObject(2, 27);

        // 实际更新的行数，没有就是0
        int n = ps.executeUpdate();
        System.out.println(n);
    }

    // delete 删除
    // delete, update和insert操作在JDBC代码层面上实际上没区别，除了sql语句不同
    @SneakyThrows
    @Test
    public void deleteQuery() {
        String sql = "DELETE FROM `users` WHERE `id` = ?";
        Connection conn = J2.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, "26");

        // 删除的行数,没有就是0
        int n = ps.executeUpdate();
        System.out.println(n);
    }

    // 总结
    // 使用JDBC执行INSERT、UPDATE和DELETE都可视为更新操作











    // 获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;

        String JDBC_URL = "jdbc:mysql://localhost:3306/fmock";
        String JDBC_USER = "root";
        String JDBC_PWD = "root";
        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    // 关闭数据库连接
    public static void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.fmock.demo.controller.jdbc;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @Author zhenhuaixiu
 * @Date 2022/11/8 14:43
 * @Version 1.0
 */
public class J1 {
    // JDBC概念
    // JDBC是java程序访问数据库的标准接口
    // 使用java访问数据库时，java代码并不是直接通过TCP连接去访问，而是通过JDBC接口来访问，而JDBC则通过JDBC驱动来实现真正的数据库访问
    // JDBC接口是Java标准库自带的，所以可以直接编译,而具体的JDBC驱动是由数据库厂商提供的，例如，MySQL的JDBC驱动由Oracle提供。
    // 因此，访问某个具体的数据库，我们只需要引入该厂商提供的JDBC驱动，就可以通过JDBC接口来访问，这样保证了Java程序编写的是一套数据库访问代码，却可以访问各种不同的数据库，因为他们都提供了标准的JDBC驱动


    // JDBC 驱动
    // JDBC是一套接口规范，它在哪呢？就在Java的标准库java.sql里放着，不过这里面大部分都是接口
    // JDBC接口并不知道我们要使用哪个数据库，所以，用哪个数据库，我们就去使用哪个数据库的“实现类”，我们把数据库实现了JDBC接口的jar包称为JDBC驱动
    // 因为我们选择了MySQL 5.x作为数据库，所以我们首先得找一个MySQL的JDBC驱动。所谓JDBC驱动，其实就是一个第三方jar包，我们直接添加一个Maven依赖就可以了:
    //<dependency>
    //    <groupId>mysql</groupId>
    //    <artifactId>mysql-connector-java</artifactId>
    //    <version>5.1.47</version>
    //    <scope>runtime</scope>
    //</dependency>


    // JDBC连接
    // 什么是JDBC连接：Connection代表一个JDBC连接，它相当于Java程序到数据库的连接（通常是TCP连接）。打开一个Connection时，需要准备URL、用户名和口令，才能成功连接到数据库。
    // URL是由数据库厂商指定的格式，例如，MySQL的URL是:
    // jdbc:mysql://<hostname>:<port>/<db>?key1=value1&key2=value2

    // 假设数据库运行在本机localhost，端口使用标准的3306，数据库名称是fmock，那么URL如下
    // 后面的两个参数表示不使用SSL加密，使用UTF-8作为字符编码（注意MySQL的UTF-8是utf8）。
    // jdbc:mysql://localhost:3306/fmock?useSSL=false&characterEncoding=utf8


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

    // JDBC查询
    //注意要点：
    //Statment和ResultSet都是需要关闭的资源，因此嵌套使用try (resource)确保及时关闭；
    //rs.next()用于判断是否有下一行记录，如果有，将自动把当前行移动到下一行（一开始获得ResultSet时当前行不是第一行）；
    //ResultSet获取列时，索引从1开始而不是0；
    //必须根据SELECT的列的对应位置来调用getLong(1)，getString(2)这些方法，否则对应位置的数据类型不对，将报错。
    @SneakyThrows
    @Test
    public void jdbcQuery() {
        Connection conn = getConnection();

        String sql = "select id,uuid,name from users";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while (result.next()) {
            System.out.println(result.getString(1));  // 注意，索引从1开始，第一个字段
            System.out.println(result.getString(2));
            System.out.println(result.getString(3));
            System.out.println("-------");
        }

        closeConn(conn);
    }

    // 使用参数绑定查询，避免sql注入
    @SneakyThrows
    @Test
    public void jdbcQuery2() {
        Connection conn = getConnection();

        String sql = "select id,uuid,name from users where id = ? and uuid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setObject(1, 3);
        stmt.setObject(2, "uuid-3");

        ResultSet result = stmt.executeQuery();

        while (result.next()) {
            System.out.println(result.getString(1));  // 注意，索引从1开始，第一个字段
            System.out.println(result.getString(2));
            System.out.println(result.getString(3));
            System.out.println("-------");
        }

        closeConn(conn);
    }


    // 数据类型
    // 使用JDBC的时候，我们需要在Java数据类型和SQL数据类型之间进行转换
    // JDBC在java.sql.Types定义了一组常量来表示如何映射SQL数据类型，但是平时我们使用的类型通常也就以下几种：
    //
    //SQL数据类型       Java数据类型
    //BIT, BOOL	         boolean
    //INTEGER	         int
    //BIGINT             long
    //REAL               float
    //FLOAT,DOUBLE       double
    //CHAR,VARCHAR       String
    //DECIMAL	         BigDecimal
    //DATE	             java.sql.Date, LocalDate
    //TIME	             java.sql.Time, LocalTime
}

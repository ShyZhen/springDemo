package com.fmock.demo.controller.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;

/**
 * @Author zhenhuaixiu
 * @Date 2022/11/8 14:43
 * @Version 1.0
 */
public class J3 {
    // 事务
    // 数据库事务具有ACID特性
    //Atomicity：原子性
    //Consistency：一致性
    //Isolation：隔离性
    //Durability：持久性

    // 事务隔离级别
    // 数据库事务可以并发执行，而数据库系统从效率考虑，对事务定义了4种不同的隔离级别，分别对应可能出现的数据不一致的情况
    //Isolation Level              脏读（Dirty Read）          不可重复读（Non Repeatable Read）          幻读（Phantom Read）
    //Read Uncommitted                    Yes                        Yes                                      Yes
    //Read Committed                       -                         Yes                                      Yes
    //Repeatable Read                      -                          -                                       Yes
    //Serializable                         -                          -                                        -


    // 事务代码示例
    @SneakyThrows
    @Test
    public void insertQuery2() {
        Connection conn = getConnection();
        try {
            // 关闭自动提交:
            conn.setAutoCommit(false);

            // 执行多条SQL语句:
            insert(); update(); delete();

            // 提交事务:
            conn.commit();

        } catch (SQLException e) {

            // 回滚事务:
            conn.rollback();
        } finally {

            // 设置回默认的自动提交
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void insert(){}
    public void update(){}
    public void delete(){}


    // batch 批处理
    // 执行batch和执行一个SQL不同点在于，需要对同一个PreparedStatement反复设置参数并调用addBatch()，这样就相当于给一个SQL加上了多组参数
    // 第二个不同点是调用的不是executeUpdate()，而是executeBatch()
    @SneakyThrows
    @Test
    public void batchInsertQuery() {
        String sql = "INSERT INTO `users` (uuid, email, mobile, name, password) VALUES (?,?,?,?,?)";
        Connection conn = J2.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setObject(1, "uuid-5");
        ps.setObject(2, "123@qq.com");
        ps.setObject(3, "13512656901");
        ps.setObject(4, "我叫老五");
        ps.setObject(5, "xxx");
        ps.addBatch();                      // 添加到batch

        ps.setObject(1, "uuid-6");
        ps.setObject(2, "123@qq.com");
        ps.setObject(3, "13512656901");
        ps.setObject(4, "我叫老六");
        ps.setObject(5, "xxx");
        ps.addBatch();                       // 添加到batch

        // 插入几条
        int[] n = ps.executeBatch();
        System.out.println(Arrays.toString(n));

        // 主键ID
        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            System.out.println(rs.getLong(1));
        }
    }



    // JDBC连接池
    // 讲多线程的时候说过，创建线程是一个昂贵的操作，如果有大量的小任务需要执行，并且频繁地创建和销毁线程，实际上会消耗大量的系统资源,所以，为了提高效率，可以用线程池
    // 类似的，JDBC如果每次操作也都来一次打开链接、关闭链接等，那么创建和销毁JDBC链接的开销就太大了，于是，也可以通过连接池（connection pool）复用已经创建好的连接

    // JDBC连接池有一个标准的接口javax.sql.DataSource，注意这个类位于Java标准库中，但仅仅是接口。
    // 要使用JDBC连接池，我们必须选择一个JDBC连接池的实现。
    // 常用的JDBC连接池有:
    //HikariCP    目前使用最广泛的就是hikariCP
    //C3P0
    //BoneCP
    //Druid



    // 以hikariCP为例：
    //
    // 1. 首先
    // 添加他的依赖
    // <dependency>
    //    <groupId>com.zaxxer</groupId>
    //    <artifactId>HikariCP</artifactId>
    //    <version>4.0.3</version>
    //    <type>bundle</type>
    // </dependency>


    // 2.接着
    // 获取数据库连接
    @SneakyThrows
    public static Connection getConnection() {
        String JDBC_URL = "jdbc:mysql://localhost:3306/fmock";
        String JDBC_USER = "root";
        String JDBC_PWD = "root";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USER);
        config.setPassword(JDBC_PWD);

        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000");      // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10");     // 最大连接数：10

        // 注意创建DataSource也是一个非常昂贵的操作，所以通常DataSource实例总是作为一个全局变量存储，并贯穿整个应用程序的生命周期
        DataSource ds = new HikariDataSource(config);

        // 有了连接池以后，我们如何使用它呢？
        // 和前面的代码类似，只是获取Connection时，把DriverManage.getConnection()改为ds.getConnection()
        // 第一次调用ds.getConnection()，会迫使连接池内部先创建一个Connection，再返回给客户端使用。
        // 当我们调用conn.close()方法时（在try(resource){...}结束处），不是真正“关闭”连接，而是释放到连接池中，以便下次获取连接时能直接返回
        Connection conn = ds.getConnection();
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


    @SneakyThrows
    @Test
    public void test() {
        Connection conn = getConnection();
        System.out.println(conn);


        String sql = "INSERT INTO `users` (uuid, email, mobile, name, password) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "uuid-5");
        ps.setObject(2, "123@qq.com");
        ps.setObject(3, "13512656901");  // 13512656901L
        ps.setObject(4, "我叫老五");
        ps.setObject(5, "xxx");
        int n = ps.executeUpdate();
        System.out.println(n);


        closeConn(conn);
    }

}

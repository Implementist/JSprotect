package com.rocky.db;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据库管理类，提供连接数据库和拆解链接功能
 *
 * @author Implementist
 */
public class DBManager extends HttpServlet {

    private ServletConfig config;                     //定义一个ServletConfig对象
    private static String username;                   //定义的数据库用户名
    private static String password;                   //定义的数据库连接密码
    private static String url;                        //定义数据库连接URL
    private static Connection connection;             //定义连接

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);                            //继承父类的init()方法
        this.config = config;                            //获取配置信息
        username = config.getInitParameter("username");    //获取数据库用户名
        password = config.getInitParameter("password");    //获取数据库连接密码
    }

    /**
     * 获得数据库连接对象
     *
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        //url中?及后面的这一段非常重要，关系到数据库中填入中文是否正确
        url = "jdbc:mysql://localhost:3306/antidebug";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    /**
     * 关闭所有的数据库连接资源
     *
     * @param connection Connection 链接
     * @param statement  Statement 资源
     * @param resultSet  ResultSet 结果集合
     */
    public static void closeAll(Connection connection, Statement statement,
                                ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 在销毁时解注册JDBC驱动
     */
    @Override
    public void destroy() {
        super.destroy();
        try {
            DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

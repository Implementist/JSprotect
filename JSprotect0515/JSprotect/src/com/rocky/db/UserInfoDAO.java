package com.rocky.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Implementist on 2017.06.01
 */
public class UserInfoDAO {

    /**
     * 向数据库中插入用户信息
     *
     * @param userInfo 用户信息
     * @return 插入是否成功
     */
    public static Boolean insertUserInfo(UserInfo userInfo) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO userinfo(username,password,emailAddress,status)");
        sqlStatement.append(" VALUES(?,?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userInfo.getUsername());
            preparedStatement.setString(2, userInfo.getPassword());
            preparedStatement.setString(3, userInfo.getEmailAddress());
            preparedStatement.setInt(4, userInfo.getStatus());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的用户名删除用户信息
     *
     * @param username 用户名
     * @return 删除是否成功
     */
    public static Boolean deleteUserInfoByUsername(String username) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM userinfo WHERE username=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的邮箱地址从数据库中删除用户信息
     *
     * @param emailAddress 邮箱地址
     * @return 删除是否成功
     */
    public static Boolean deleteUserInfoByEmailAddress(String emailAddress) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM userinfo WHERE emailAddress=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, emailAddress);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 更改指定用户的权限
     * @param username 用户名
     * @param status 权限
     * @return 更改是否成功
     */
    public static Boolean updateStatusByUsername(String username, int status){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE userinfo SET status=? WHERE username=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息对象
     */
    public static UserInfo queryUserInfoByUsername(String username) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM userinfo WHERE username=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            UserInfo userInfo = new UserInfo();
            if (resultSet.next()) {
                userInfo.setUsername(username);
                userInfo.setEmailAddress(resultSet.getString("emailAddress"));
                userInfo.setPassword(resultSet.getString("password"));
                userInfo.setStatus(resultSet.getInt("status"));
                return userInfo;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 查询具有指定权限的用户
     *
     * @param status 权限
     * @return 具有指定权限的用户列表
     */
    public static ArrayList<UserInfo> queryUserInfoByStatus(int status) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();

        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM userinfo WHERE status=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, status);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(resultSet.getString("username"));
                userInfo.setPassword(resultSet.getString("password"));
                userInfo.setEmailAddress(resultSet.getString("emailAddress"));
                userInfo.setStatus(status);

                userInfos.add(userInfo);
            }
            return userInfos;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return userInfos;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 查询数据库中所有用户
     *
     * @return 数据库中所有用户的列表
     */
    public static ArrayList<UserInfo> queryAll() {
        ArrayList<UserInfo> userInfos = new ArrayList<>();

        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();

        //查询所有用户，输出表按权限降序、用户名升序的规则排序
        sqlStatement.append("SELECT * FROM userinfo ORDER BY status DESC, username ASC");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(resultSet.getString("username"));
                userInfo.setPassword(resultSet.getString("password"));
                userInfo.setEmailAddress(resultSet.getString("emailAddress"));
                userInfo.setStatus(resultSet.getInt("status"));

                userInfos.add(userInfo);
            }
            return userInfos;
        } catch (SQLException ex) {
            Logger.getLogger(UserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return userInfos;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
}

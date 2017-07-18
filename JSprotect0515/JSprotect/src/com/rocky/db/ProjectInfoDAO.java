package com.rocky.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Implementist on 2017.06.01
 */
public class ProjectInfoDAO {

    /**
     * 向数据库中插入工程信息
     *
     * @param projectInfo 工程信息
     * @return 插入是否成功
     */
    public static Boolean insertProjectInfo(ProjectInfo projectInfo) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO project_info(username,project_id,anti_dbg,obfuscation,");
        sqlStatement.append("anti_tamper,date,obfuscation_strength,flattern_count,opaque_count,fileName)");
        sqlStatement.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, projectInfo.getUsername());
            preparedStatement.setString(2, projectInfo.getProjectId());
            preparedStatement.setInt(3, projectInfo.getAntidbg());
            preparedStatement.setInt(4, projectInfo.getObfuscation());
            preparedStatement.setInt(5, projectInfo.getAntiTamper());

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            preparedStatement.setTimestamp(6, timestamp);
            preparedStatement.setString(7, projectInfo.getObfuscationStrength());
            preparedStatement.setInt(8, projectInfo.getFlatternCount());
            preparedStatement.setInt(9, projectInfo.getOpaqueCount());
            preparedStatement.setString(10,projectInfo.getFileName());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的用户名删除该用户名下的所有工程
     *
     * @param username 用户名
     * @return 删除是否成功
     */
    public static Boolean deleteProjectInfoByUsername(String username) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM project_info WHERE username=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的用户名和工程Id删除工程信息
     *
     * @param projectId 工程Id
     * @return 删除是否成功
     */
    public static Boolean deleteProjectInfoByUsernameAndProjectId(String username, String projectId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM project_info WHERE username=? AND project_id=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, projectId);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定的用户名和工程Id更改是否可运行字段
     * @param username 用户名
     * @param projectId 工程Id
     * @return 更改是否成功
     */
    public static Boolean updateRunnableByUsernameAndProjectId(String username, String projectId){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE project_info ");
        sqlStatement.append("SET runnable = (runnable + 1) % 2 " );
        sqlStatement.append("WHERE username=? AND project_id=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, projectId);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBManager.closeAll(connection, preparedStatement, null);
        }
    }

    /**
     * 按指定用户名查询该用户名下的所有工程
     *
     * @param username 用户名
     * @return 该用户名下的所有工程的ArrayList
     */
    public static ArrayList<ProjectInfo> queryProjectInfoByUsername(String username) {
        ArrayList<ProjectInfo> projectInfos = new ArrayList<>();

        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM project_info WHERE username=? ");
        sqlStatement.append("ORDER BY project_id ASC");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setUsername(resultSet.getString("username"));
                projectInfo.setProjectId(resultSet.getString("project_id"));
                projectInfo.setAntidbg(resultSet.getInt("anti_dbg"));
                projectInfo.setObfuscation(resultSet.getInt("obfuscation"));
                projectInfo.setAntiTamper(resultSet.getInt("anti_tamper"));
                projectInfo.setDate(resultSet.getTimestamp("date"));
                projectInfo.setObfuscationStrength(resultSet.getString("obfuscation_strength"));
                projectInfo.setFlatternCount(resultSet.getInt("flattern_count"));
                projectInfo.setOpaqueCount(resultSet.getInt("opaque_count"));
                projectInfo.setRunnable(resultSet.getBoolean("runnable"));
                projectInfo.setFileName(resultSet.getString("fileName"));

                projectInfos.add(projectInfo);
            }
            return projectInfos;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return projectInfos;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 按指定用户和工程Id查询工程信息
     *
     * @param username  用户名
     * @param projectId 工程Id
     * @return 工程信息
     */
    public static ProjectInfo queryProjectInfoByUsernameAndProjectId(String username, String projectId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM project_info WHERE username=? And project_id=?");

        ProjectInfo projectInfo = new ProjectInfo();

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, projectId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                projectInfo.setUsername(resultSet.getString("username"));
                projectInfo.setProjectId(resultSet.getString("project_id"));
                projectInfo.setAntidbg(resultSet.getInt("anti_dbg"));
                projectInfo.setObfuscation(resultSet.getInt("obfuscation"));
                projectInfo.setAntiTamper(resultSet.getInt("anti_tamper"));
                projectInfo.setDate(resultSet.getTimestamp("date"));
                projectInfo.setObfuscationStrength(resultSet.getString("obfuscation_strength"));
                projectInfo.setFlatternCount(resultSet.getInt("flattern_count"));
                projectInfo.setOpaqueCount(resultSet.getInt("opaque_count"));
                projectInfo.setRunnable(resultSet.getBoolean("runnable"));
                projectInfo.setFileName(resultSet.getString("fileName"));
            }

            return projectInfo;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return projectInfo;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 查询指定用户名下的工程总数
     *
     * @param username 用户名
     * @return 用户名下的工程总数
     */
    public static int queryCountByUsername(String username) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT COUNT(*) FROM project_info WHERE username=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
}

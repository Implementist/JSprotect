package com.rocky.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Implementist on 2017/6/17.
 */
public class ErrorDAO {
    public static ArrayList<Error> queryAll() {
        ArrayList<Error> errors = new ArrayList<>();

        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM error");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Error error = new Error();
                error.setErrorId(resultSet.getInt("errorId"));
                error.setUsername(resultSet.getString("username"));
                error.setProjectId(resultSet.getString("projectId"));
                error.setErrorType(resultSet.getString("errorType"));
                error.setErrorContent(resultSet.getString("errorContent"));

                errors.add(error);
            }
            return errors;
        } catch (SQLException ex) {
            Logger.getLogger(ErrorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return errors;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
}

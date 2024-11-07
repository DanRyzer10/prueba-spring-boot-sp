package com.danam.iroute.routest.db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection;

    public static Connection createConnection() throws ClassNotFoundException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_irouteprueba","root","");

        }catch (SQLException e){
            e.printStackTrace();
        }

        return connection;
    }

}

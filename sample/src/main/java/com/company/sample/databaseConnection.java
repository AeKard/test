
package com.company.sample;
import java.sql.*;

public class databaseConnection {
    private String dbName; // fix this tp much use of memory
    private Connection con;
    private boolean islogin = false;

    databaseConnection(String dbName)
    {
        this.dbName = dbName;
    } 

    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "lorddrakebazaK15");
            
            if(!databaseExists(con, dbName)) 
            {
                System.out.println("Creating Database!");
                createDatabase(con, dbName);
            }
            con.setCatalog(dbName); createUserTable(con);
            
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    private static boolean databaseExists(Connection con, String dbName)
    throws SQLException{
        String query = "SELECT SCHEMA_NAME " +
                       "FROM INFORMATION_SCHEMA.SCHEMATA " +
                       "WHERE SCHEMA_NAME = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query))
        {
            preparedStatement.setString(1, dbName);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }

    private static void createDatabase(Connection con, String dbName)
    throws SQLException
    {
        String query = "CREATE DATABASE " + dbName;
        try(PreparedStatement preparedStatement = con.prepareStatement(query))
        {
            preparedStatement.executeUpdate();
        }
    }   

    private static void createUserTable(Connection con)
    throws SQLException
    {
        String query =  "CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "username VARCHAR(255) NOT NULL," +
            "password VARCHAR(255) NOT NULL," +
            "email VARCHAR(255) NOT NULL)";

        try(PreparedStatement preparedStatement = con.prepareStatement(query))
        {
            preparedStatement.executeUpdate();
        }
    }   

    public void dataInsert(String user,String pass, String email)
    throws SQLException {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        }
    }
    public void login(String user, String pass)
    throws SQLException
    {
        String query = "select * from users where username = ? and password = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query))
        {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            ResultSet result = preparedStatement.executeQuery();

            if(!result.next())
            {
                System.out.print("no data");
                islogin = false;
            }
            else System.out.print("continue");
        }
    }

    public boolean isLogin()
    {
        return islogin;
    }
}

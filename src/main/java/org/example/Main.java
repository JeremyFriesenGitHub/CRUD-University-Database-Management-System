package org.example;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url ="jdbc:postgresql://localhost:5432/university";
        String user ="postgres";
        String password = "postgres";

        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null){
                System.out.println("Connected to the database" + "\n");
            }
            else{
                System.out.println("Failed to connect to the database");
            }

            //control
            getAllStudents(connection);

            connection.close();

        }catch (Exception e){
            System.out.println(e);

        }
    }

    public static void getAllStudents(Connection connection){
        try{
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students ORDER BY student_id ASC");
            ResultSet resultSet = statement.getResultSet();
            String header = "ID \tName \t\tEmail \t\t\t\t\tEnrollment Date";
            System.out.println(header);
            System.out.println("-------------------------------------------------------");
            while(resultSet.next()){
                System.out.print(resultSet.getInt("student_id") + " \t");
                System.out.print(resultSet.getString("first_name") + " ");
                System.out.print(resultSet.getString("last_name") + " \t");
                System.out.print(resultSet.getString("email") + " \t");
                System.out.println(resultSet.getDate("enrollment_date"));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }


}
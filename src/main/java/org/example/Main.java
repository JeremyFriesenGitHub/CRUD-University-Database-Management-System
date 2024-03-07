package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        String url ="jdbc:postgresql://localhost:5432/university";
        String user ="postgres";
        String password = "postgres";
        Scanner scanner = new Scanner(System.in);

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
            addStudent(connection, scanner);
            getAllStudents(connection);




        }catch (Exception e){
            System.out.println("Error: ");
            e.printStackTrace();
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
            System.out.println("Error getting students: ");
            e.printStackTrace();
        }
    }

    public static void addStudent(Connection connection, Scanner scanner){
        String first_name, last_name, email;
        String dateString;
        Date date;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("\nYou are adding a student!");

        System.out.println("What is the student's first name?: ");
        first_name = scanner.nextLine();

        System.out.println("What is the student's last name?: ");
        last_name = scanner.nextLine();

        System.out.println("What is the student's email?: ");
        email = scanner.nextLine();

        System.out.println("What is their enrollment date (yyyy-mm-dd)? : ");
        dateString = scanner.nextLine();
        try{
            java.util.Date utilDate = dateFormatter.parse(dateString);
            date = new Date(utilDate.getTime());
        } catch (Exception e){
            System.out.println("Error parsing the date: ");
            e.printStackTrace();
            return;
        }

        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setDate(4, date);

            statement.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (Exception e){
            System.out.println("Error inserting student: ");
            e.printStackTrace();
        }
    }

//    public static void deleteStudent(Connection connection, Scanner scanner){
//
//    }


}
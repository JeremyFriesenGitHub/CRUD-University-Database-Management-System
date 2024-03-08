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


        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            control(connection);
            connection.close();

        }catch (Exception e){
            System.out.println("Error connecting to database: ");
            e.printStackTrace();
        }
    }

    public static void getAllStudents(Connection connection){
        try{
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students ORDER BY student_id ASC");
            ResultSet resultSet = statement.getResultSet();

            System.out.printf("%-5s %-20s %-30s %-15s\n", "ID", "Name", "Email", "Enrollment Date");
            System.out.println("---------------------------------------------------------------------------------");

            while(resultSet.next()){
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");

                System.out.printf("%-5d %-20s %-30s %-15s\n",
                        resultSet.getInt("student_id"),
                        fullName,
                        resultSet.getString("email"),
                        resultSet.getDate("enrollment_date").toString());
            }
        } catch (Exception e){
            System.out.println("Error getting students: ");
            e.printStackTrace();
        }
    }

    public static void addStudent(Connection connection, Scanner scanner){

        Date date;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("\nYou are adding a student!");

        System.out.println("What is the student's first name?: ");
        String first_name = scanner.nextLine();

        System.out.println("What is the student's last name?: ");
        String last_name = scanner.nextLine();

        System.out.println("What is the student's email?: ");
        String email = scanner.nextLine();

        System.out.println("What is their enrollment date (yyyy-mm-dd)? : ");
        String dateString = scanner.nextLine();
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

    public static void deleteStudent(Connection connection, Scanner scanner){

        System.out.println("\nYou are deleting a student!");

        System.out.println("What is the student's ID?: ");
        int student_id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
        } catch (Exception e) {
            System.out.println("Error deleting student: ");
            e.printStackTrace();
        }

    }

    public static void updateEmail(Connection connection, Scanner scanner) {
        System.out.println("\nYou are updating a student's email!");

        System.out.println("What is the student's ID?: ");

        int student_id = Integer.parseInt(scanner.nextLine());

        System.out.println("What is the new email that you would like to update to?: ");
        String email = scanner.nextLine();

        String sql = "UPDATE students SET email = ? WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the parameters for the prepared statement
            statement.setString(1, email);
            statement.setInt(2, student_id);


            int affectedRows = statement.executeUpdate();


            if (affectedRows > 0) {
                System.out.println("Email updated successfully.");
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
        } catch (Exception e) {
            System.out.println("Error updating student email: ");
            e.printStackTrace();
        }
    }

    public static void control(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n\nWelcome to the 3005 Database!\n");
            System.out.println("What would you like to do:");
            System.out.println("  (1) View all students");
            System.out.println("  (2) Add student");
            System.out.println("  (3) Delete student");
            System.out.println("  (4) Update student email");
            System.out.println("  (0) Exit\n");

            System.out.print("Enter your selection: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    getAllStudents(connection);
                    break;
                case 2:
                    addStudent(connection, scanner);
                    break;
                case 3:
                    deleteStudent(connection, scanner);
                    break;
                case 4:
                    updateEmail(connection, scanner);
                    break;
                case 0:
                    // Optionally handle exit case if needed
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    // Removed the redundant scanner.nextInt() call
                    break;
            }
        } while (choice != 0);
        scanner.close();
    }
}
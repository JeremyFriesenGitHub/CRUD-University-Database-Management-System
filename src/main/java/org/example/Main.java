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
        //credentials
        String url ="jdbc:postgresql://localhost:5432/university";
        String user ="postgres";
        String password = "postgres";

        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            //call main control flow view
            control(connection);

            //close connection
            connection.close();

            //error
        }catch (Exception e){
            System.out.println("Error connecting to database: ");
            e.printStackTrace();
        }
    }

    //getAllStudents
    public static void getAllStudents(Connection connection){
        try{
            //select all students into resultSet
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students ORDER BY student_id ASC");
            ResultSet resultSet = statement.getResultSet();

            //header
            System.out.printf("%-5s %-20s %-30s %-15s\n", "ID", "Name", "Email", "Enrollment Date");
            System.out.println("---------------------------------------------------------------------------------");

            while(resultSet.next()){

                //fullName result set
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");

                //print out formatted data
                System.out.printf("%-5d %-20s %-30s %-15s\n",
                        resultSet.getInt("student_id"),
                        fullName,
                        resultSet.getString("email"),
                        resultSet.getDate("enrollment_date").toString());
            }
            //error
        } catch (Exception e){
            System.out.println("Error getting students: ");
            e.printStackTrace();
        }
    }

    //addStudent
    public static void addStudent(Connection connection, Scanner scanner){
        //init
        Date date;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        //receive input
        System.out.println("\nYou are adding a student!");

        System.out.println("What is the student's first name?: ");
        String first_name = scanner.nextLine();

        System.out.println("What is the student's last name?: ");
        String last_name = scanner.nextLine();

        System.out.println("What is the student's email?: ");
        String email = scanner.nextLine();

        System.out.println("What is their enrollment date (yyyy-mm-dd)? : ");
        String dateString = scanner.nextLine();

        //format date
        try{
            java.util.Date utilDate = dateFormatter.parse(dateString);
            date = new Date(utilDate.getTime());

            //error
        } catch (Exception e){
            System.out.println("Error parsing the date: ");
            e.printStackTrace();
            return;
        }
        //sql query
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

        //create insert update with info
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setDate(4, date);

            statement.executeUpdate();
            System.out.println("Student added successfully!");

            //error
        } catch (Exception e){
            System.out.println("Error inserting student: ");
            e.printStackTrace();
        }
    }

    //deleteStudent
    public static void deleteStudent(Connection connection, Scanner scanner){

        System.out.println("\nYou are deleting a student!");

        //get input
        System.out.println("What is the student's ID?: ");
        int student_id = Integer.parseInt(scanner.nextLine());

        //query string
        String sql = "DELETE FROM students WHERE student_id = ?";

        //delete student
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student_id);

            int rowsAffected = statement.executeUpdate();

            //sucess message
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");

                //error message
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
            //error
        } catch (Exception e) {
            System.out.println("Error deleting student: ");
            e.printStackTrace();
        }
    }

    //updateEmail
    public static void updateEmail(Connection connection, Scanner scanner) {

        System.out.println("\nYou are updating a student's email!");

        //input
        System.out.println("What is the student's ID?: ");

        int student_id = Integer.parseInt(scanner.nextLine());

        System.out.println("What is the new email that you would like to update to?: ");
        String email = scanner.nextLine();

        //query string
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";

        //executeUpdate
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setInt(2, student_id);
            int affectedRows = statement.executeUpdate();

            //success message
            if (affectedRows > 0) {
                System.out.println("Email updated successfully.");
                //error message
            } else {
                System.out.println("No student found with ID: " + student_id);
            }
            //error
        } catch (Exception e) {
            System.out.println("Error updating student email: ");
            e.printStackTrace();
        }
    }

    //main control flow
    public static void control(Connection connection) {
        //init
        Scanner scanner = new Scanner(System.in);
        int choice;

        //loop for main control view
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

            //call previous functions in loop
            switch (choice) {
                case 1 -> getAllStudents(connection);
                case 2 -> addStudent(connection, scanner);
                case 3 -> deleteStudent(connection, scanner);
                case 4 -> updateEmail(connection, scanner);
                case 0 -> System.out.println("Exiting!");
                default -> System.out.println("Invalid selection. Please try again."); //continue looping for invalid input
            }
        } while (choice != 0); //exit when choice is 0

        //close scanner
        scanner.close();
    }
}
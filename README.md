# University Database Management System

## Overview

The University Database Management System (UDMS) is a Java-based application designed to facilitate the management of university data, specifically focusing on student records. It allows administrators to perform CRUD (Create, Read, Update, Delete) operations on student data stored in a PostgreSQL database. The application provides a simple, console-based interface for interacting with the database.

## Features

- **View All Students**: Display a list of all students, including their ID, name, email, and enrollment date.
- **Add Student**: Insert a new student record into the database.
- **Delete Student**: Remove a student record from the database based on the student's ID.
- **Update Student Email**: Modify the email address of an existing student record.

## Requirements

- Java Development Kit (JDK) 8 or later.
- PostgreSQL Database Server.
- PostgreSQL JDBC Driver (included in the project dependencies).

## Setup

1. **Database Preparation**:
   - Ensure PostgreSQL is installed and running on your system.
   - Create a database named `university` using pgAdmin4. 
   - Use the provided [SQL scripts](https://github.com/JeremyFriesenGitHub/COMP3005_Assignment-3_Question-1/blob/main/init.sql) to create the students table and populate it with initial data. You can use the query tool to do this as shown in the [tutorial video](https://youtu.be/6o1gHDBhOTU). 

2. **Configuration**:
   - Open the `Main` class and locate the database connection setup section.
   - Replace the `url`, `user`, and `password` variables with your PostgreSQL database details.

3. **Running the Application**:
   - Compile the Java files and run the `Main` class.
   - The application will connect to the specified PostgreSQL database and present the main menu for database operations.

4. **Tutorial Video**:
   - For a comprehensive tutorial of the setup and usage of the application, a tutorial video is available [here](https://youtu.be/6o1gHDBhOTU). 

## Usage

After starting the application, use the console menu to choose an action by entering the number corresponding to the desired operation and following the on-screen instructions. For example, to add a new student, input option 2 and provide the required student details as prompted.

## License

This project is open-source and available under the MIT License. See the [LICENSE](https://github.com/JeremyFriesenGitHub/COMP3005_Assignment-3_Question-1/blob/main/LICENSE) file for more details.

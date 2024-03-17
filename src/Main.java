import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private final String url = "jdbc:postgresql://localhost:5432/database_name";
    private final String user = "username";
    private final String password = "password";

    public void getAllStudents() {
        String SQL = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int student_id = rs.getInt("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String enrollment_date = rs.getString("enrollment_date");
                System.out.println("Student ID: " + student_id + ", Name: " + first_name + " " + last_name +
                        ", Email: " + email + ", Enrollment Date: " + enrollment_date);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addStudent(String first_name, String last_name, String email, String enrollment_date) {
        String SQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?::DATE)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, java.sql.Date.valueOf(enrollment_date)); // Convert string to java.sql.Date
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateStudentEmail(int student_id, String new_email) {
        String SQL = "UPDATE students SET email = ? WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            pstmt.executeUpdate();
            System.out.println("Student email updated!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteStudent(int student_id) {
        String SQL = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, student_id);
            pstmt.executeUpdate();
            System.out.println("Student deleted!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Main database = new Main();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Get all students");
            System.out.println("2. Add a student");
            System.out.println("3. Update student email");
            System.out.println("4. Delete student");
            System.out.println("5. Exit");
            System.out.println("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    database.getAllStudents();
                    break;
                case "2":
                    System.out.println("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.println("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.println("Enter enrollment date: ");
                    String enrollmentDate = scanner.nextLine();
                    database.addStudent(firstName, lastName, email, enrollmentDate);
                    break;
                case "3":
                    System.out.println("Enter student id: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    database.updateStudentEmail(studentId, newEmail);
                    break;
                case "4":
                    System.out.println("Enter student id: ");
                    int deleteStudentId = Integer.parseInt(scanner.nextLine());
                    database.deleteStudent(deleteStudentId);
                    break;
                case "5":
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}

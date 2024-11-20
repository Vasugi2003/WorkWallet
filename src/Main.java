import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Database credentials
        String url = "jdbc:mysql://localhost:3306/WeeklyEarnings";
        String username = "root";  // Change to your MySQL username
        String password = "root";  // Change to your MySQL password

        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection connection = DriverManager.getConnection(url, username, password);

            int choice;
            do {
                // Display menu
                System.out.println("\n===== Weekly Earnings System =====");
                System.out.println("1. Add Data");
                System.out.println("2. View Data by Date");
                System.out.println("3. Delete Data");
                System.out.println("4. View Total Earnings");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addData(connection, scanner);
                        break;
                    case 2:
                        viewDataByDate(connection, scanner);
                        break;
                    case 3:
                        deleteData(connection, scanner);
                        break;
                    case 4:
                        viewTotalEarnings(connection);
                        break;
                    case 5:
                        System.out.println("Exiting system...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);

            // Close connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add data
    private static void addData(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter Day (Monday, Tuesday, etc.): ");
            String day = scanner.nextLine();
            System.out.print("Enter number of pieces: ");
            int pieces = scanner.nextInt();

            String query = "INSERT INTO pieces (date, day, pieces) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, day);
            preparedStatement.setInt(3, pieces);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding data: " + e.getMessage());
        }
    }

    // Method to view data by date
    private static void viewDataByDate(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            String query = "SELECT * FROM pieces WHERE date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\n+------------+------------+--------+");
            System.out.println("|    Date    |    Day     | Pieces |");
            System.out.println("+------------+------------+--------+");

            boolean dataFound = false;
            while (resultSet.next()) {
                String day = resultSet.getString("day");
                int pieces = resultSet.getInt("pieces");
                System.out.printf("| %-10s | %-10s | %-6d |\n", date, day, pieces);
                dataFound = true;
            }

            System.out.println("+------------+------------+--------+");

            if (!dataFound) {
                System.out.println("No data found for date: " + date);
            }
        } catch (SQLException e) {
            System.out.println("Error viewing data: " + e.getMessage());
        }
    }

    // Method to delete data
    private static void deleteData(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Date (YYYY-MM-DD) to delete: ");
            String date = scanner.nextLine();

            String query = "DELETE FROM pieces WHERE date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data deleted successfully!");
            } else {
                System.out.println("No data found for date: " + date);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting data: " + e.getMessage());
        }
    }

    // Method to view total earnings
    private static void viewTotalEarnings(Connection connection) {
        try {
            String query = "SELECT WEEK(date) AS week_number, SUM(pieces) AS total_pieces " +
                           "FROM pieces GROUP BY WEEK(date)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\n+------------+------------+------------+");
            System.out.println("| Week Number | Total Pieces | Earnings  |");
            System.out.println("+------------+------------+------------+");

            boolean dataFound = false;

            while (resultSet.next()) {
                int weekNumber = resultSet.getInt("week_number");
                int totalPieces = resultSet.getInt("total_pieces");
                double earnings = totalPieces * 0.60; // 60 paise per piece
                System.out.printf("| %-10d | %-12d | Rs %-9.2f |\n", weekNumber, totalPieces, earnings);
                dataFound = true;
            }

            System.out.println("+------------+------------+------------+");

            if (!dataFound) {
                System.out.println("No data found.");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating earnings: " + e.getMessage());
        }
    }
}

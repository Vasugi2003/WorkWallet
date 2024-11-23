import java.sql.*;
import java.util.Scanner;

// Base class for common database operations
abstract class DatabaseOperations {
    protected Connection connection;

    public DatabaseOperations(Connection connection) {
        this.connection = connection;
    }

    public abstract void addData(Scanner scanner);
    public abstract void viewDataByDate(Scanner scanner);
    public abstract void deleteData(Scanner scanner);
    public abstract void viewTotalEarnings();
}

// Derived class implementing database operations
class WorkWalletOperations extends DatabaseOperations {

    public WorkWalletOperations(Connection connection) {
        super(connection);
    }

    @Override
    public void addData(Scanner scanner) {
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
        } finally {
            scanner.nextLine(); // Clear the buffer
        }
    }

    @Override
    public void viewDataByDate(Scanner scanner) {
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

    @Override
    public void deleteData(Scanner scanner) {
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

    @Override
    public void viewTotalEarnings() {
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

// Utility class for database connection
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/WeeklyEarnings";
    private static final String USERNAME = "root";  // Change to your MySQL username
    private static final String PASSWORD = "your_password";  // Change to your MySQL password

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}

// Main class to run the application
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConnection.connect();

        if (connection == null) {
            System.out.println("Unable to establish database connection. Exiting...");
            return;
        }

        WorkWalletOperations operations = new WorkWalletOperations(connection);

        int choice;
        do {
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
                    operations.addData(scanner);
                    break;
                case 2:
                    operations.viewDataByDate(scanner);
                    break;
                case 3:
                    operations.deleteData(scanner);
                    break;
                case 4:
                    operations.viewTotalEarnings();
                    break;
                case 5:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}

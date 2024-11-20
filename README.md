**WORK WALLET**
## Overview
The **WORK WALLET** is a Java console application designed to help track and manage checking work earnings in a textile induatry on a weekly basis. It allows users to record daily production data, calculate weekly earnings, and organize records efficiently. The application connects to a MySQL database for persistent data storage and retrieval.

### Features
1. **Add Data**: Enter daily records of production, including date, day, and the number of pieces completed.
2. **View Data by Date**: Display production records for a specific date in a tabular format.
3. **Delete Data**: Remove incorrect or unwanted records based on date.
4. **Weekly Earnings Summary**: Calculate total weekly earnings automatically based on the number of pieces produced.
5. **Database Integration**: Uses MySQL for reliable and persistent data storage.
6. **Tabular Display**: Displays data in an easy-to-read table format in the console.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
- **MySQL Server**: A running MySQL database instance.
- **MySQL JDBC Driver**: Download and add the MySQL JDBC driver (`mysql-connector-java-X.X.X.jar`) to your project's classpath.
- **IDE or Text Editor**: You can use any IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code.

### Installation

1. **Clone the Repository**:
   ```bash
           https://github.com/Vasugi2003/WorkWallet
   ```

2. **Navigate to the Project Directory**:
   ```bash
   cd WotkWallet
   ```

3. **Set Up Database**:
   - Log in to MySQL:
     ```bash
     mysql -u root -p
     ```
   - Create the `WeeklyEarnings` database:
     ```sql
     CREATE DATABASE WeeklyEarnings;
     ```
   - Create the `pieces` table:
     ```sql
     USE WeeklyEarnings;

     CREATE TABLE pieces (
         id INT AUTO_INCREMENT PRIMARY KEY,
         date DATE NOT NULL,
         day VARCHAR(20) NOT NULL,
         pieces INT NOT NULL
     );
     ```

4. **Update Database Credentials**:
   - Open the `Main.java` file and update the following variables with your MySQL credentials:
     ```java
     String url = "jdbc:mysql://localhost:3306/WeeklyEarnings";
     String username = "your_mysql_username";
     String password = "your_mysql_password";
     ```

5. **Compile the Java Files**:
   ```bash
   javac -d bin src/*.java
   ```

6. **Run the Application**:
   ```bash
   java -cp bin Main
   ```

---

## Usage

### Main Menu
When you run the program, you’ll see the following menu options:
```
===== Weekly Earnings System =====
1. Add Data
2. View Data by Date
3. Delete Data
4. View Total Earnings
5. Exit
Enter your choice:
```

### Example Operations
#### Add Data
- Enter the date, day, and the number of pieces produced.
- The system will save the record to the database.

#### View Data by Date
- Enter a specific date (e.g., `2024-11-20`).
- The system will display all records for that date in a table format.

#### Delete Data
- Provide the date to delete records.
- The system will confirm whether the deletion was successful.

#### View Total Earnings
- The system calculates total weekly earnings automatically based on the number of pieces produced, assuming each piece earns 60 paise.

---

## Database Schema

```sql
CREATE TABLE pieces (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    day VARCHAR(20) NOT NULL,
    pieces INT NOT NULL
);
```

---

## Built With
- **Java**: Core application logic.
- **MySQL**: Database for storing records.
- **JDBC**: For connecting Java with MySQL.

---

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to customize and expand this content based on additional features or updates to your project. Add screenshots of your application to enhance clarity!

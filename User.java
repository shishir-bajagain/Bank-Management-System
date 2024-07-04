import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class User {
    static Random random = new Random();

    public void setRegister(Connection connection, Scanner scanner, String responseName, String responseContact, String responsePassword) {
        if (!Accounts.checkAccountByEmail(connection, responseContact)) {
            try {
                int responseId = generateUniqueId(connection);
                System.out.print("Enter your Initial Balance amount: ");
                int responseBalance = scanner.nextInt();
                scanner.nextLine();

                String query = "INSERT INTO users(user_name, user_contact, user_id, user_password, user_balance) VALUES(?, ?, ?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, responseName);
                preparedStatement.setString(2, responseContact);
                preparedStatement.setString(3, String.valueOf(responseId));
                preparedStatement.setString(4, responsePassword);
                preparedStatement.setInt(5, responseBalance);
                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected > 0) {
                    System.out.println("Successfully Registered!");
                } else {
                    System.out.println("Error in registration!");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("\nAccount with this email already exists!! \n");
        }
    }

    private int generateUniqueId(Connection connection) throws SQLException {
        boolean checker;
        int responseId;
        do {
            responseId = 10000000 + random.nextInt(1000);
            String query = "SELECT user_id FROM users WHERE user_id = ?";
            PreparedStatement forId = connection.prepareStatement(query);
            forId.setString(1, String.valueOf(responseId));
            ResultSet res = forId.executeQuery();
            checker = res.next();
        } while (checker);
        return responseId;
    }

    public void setLogin(Connection connection, Scanner scanner, String responseContact, String responsePassword) {
        if (Accounts.checkAccount(connection, responseContact, responsePassword, 1)) {
            System.out.println("Logged out!!!");
        } else {
            System.out.println("\nError! Check your username and Password!!");
        }
    }
}
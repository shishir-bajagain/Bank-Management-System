import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    public static void entryPoint(Connection connection, Scanner scanner, ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("user_name");
        String id = resultSet.getString("user_id");
        float balance = resultSet.getFloat("user_balance");
        String password = resultSet.getString("user_password");
        while (true) {
            System.out.printf("*******Welcome to Dashboard, %s*******\n", name);
            System.out.println();
            System.out.println("""
                    1. Deposit cash
                    2. Withdraw cash
                    3. Transfer cash
                    4. Exit to login
                    """);
            System.out.print("\nEnter your response: ");
            int response = getUserIntInput(scanner);
            switch (response) {
                case 1:
                    addMoney(connection, scanner, id, password);
                    break;
                case 2:
                    withdrawMoney(connection, scanner, id, balance, password);
                    break;
                case 3:
                    transferMoney(connection, scanner, id, password);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Error Response!!! Enter Again");
            }
        }
    }

    private static void addMoney(Connection connection, Scanner scanner, String id, String password) throws SQLException {
        if (verifyPassword(scanner, password)) {
            System.out.print("\nInsert the amount of Money to deposit: ");
            int addMoney = getUserIntInput(scanner);
            PreparedStatement prep1 = connection.prepareStatement("UPDATE users SET user_balance = user_balance + ? WHERE user_id = ?;");
            prep1.setInt(1, addMoney);
            prep1.setString(2, id);
            int rowsAffected = prep1.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully deposited Rs." + addMoney);
            } else {
                System.out.println("Error!!");
            }
        } else {
            System.out.println("Incorrect password! Transaction aborted.");
        }
    }

    private static void withdrawMoney(Connection connection, Scanner scanner, String id, float balance, String password) throws SQLException {
        if (verifyPassword(scanner, password)) {
            float withdrawMoney;
            System.out.print("\nInsert the amount of Money to Withdraw: ");
            withdrawMoney = getUserFloatInput(scanner);
            do {
                if (withdrawMoney > balance) {
                    System.out.print("Cannot process the Transaction!!\n\n1.Retry\n2. Return to DashBoard\nEnter Your Response: ");
                    switch (getUserIntInput(scanner)) {
                        case 1:
                            System.out.println("\nInsert the amount of Money to Withdraw: ");
                            withdrawMoney = getUserFloatInput(scanner);
                            break;
                        case 2:
                            return;
                        default:
                            System.out.println("Error!! Enter valid value");
                    }
                } else {
                    break;
                }
            } while (true);
            PreparedStatement prep1 = connection.prepareStatement("UPDATE users SET user_balance = user_balance - ? WHERE user_id = ?;");
            prep1.setFloat(1, withdrawMoney);
            prep1.setString(2, id);
            int rowsAffected = prep1.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully withdrawn Rs." + withdrawMoney);
            } else {
                System.out.println("Error!!");
            }
        } else {
            System.out.println("Incorrect password! Transaction aborted.");
        }
    }

    private static void transferMoney(Connection connection, Scanner scanner, String userId, String password) throws SQLException {
        if (verifyPassword(scanner, password)) {
            try {
                connection.setAutoCommit(false);
                scanner.nextLine();
                System.out.print("Enter the receiver account Id: ");
                String givenId = scanner.nextLine();
                System.out.print("Enter the amount to Transfer: ");
                float amount = getUserFloatInput(scanner);
                PreparedStatement deposit = connection.prepareStatement("UPDATE users SET user_balance = user_balance + ? WHERE (user_id = ? OR user_contact = ?)");
                PreparedStatement withdraw = connection.prepareStatement("UPDATE users SET user_balance = user_balance - ? WHERE user_id = ?");
                deposit.setFloat(1, amount);
                deposit.setString(2, givenId);
                deposit.setString(3, givenId);
                withdraw.setFloat(1, amount);
                withdraw.setString(2, userId);
                int depositRow = deposit.executeUpdate();
                int withdrawRow = withdraw.executeUpdate();
                if (depositRow > 0 && withdrawRow > 0) {
                    System.out.println("Transaction successful!!!");
                    connection.commit();
                } else {
                    System.out.println("Error!! Transaction Failed");
                    connection.rollback();
                }
            } catch (SQLException e) {
                System.out.println("Error!!! Try Again");
                connection.rollback();
            }
        } else {
            System.out.println("Incorrect password! Transaction aborted.");
        }
    }

    private static boolean verifyPassword(Scanner scanner, String password) {
        System.out.print("Please enter your password for verification: ");
        String inputPassword = scanner.nextLine();
        return password.equals(inputPassword);
    }

    private static int getUserIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static float getUserFloatInput(Scanner scanner) {
        while (!scanner.hasNextFloat()) {
            System.out.println("Invalid input, please enter a number.");
            scanner.next();
        }
        return scanner.nextFloat();
    }
}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Accounts {

    public static boolean checkAccount(Connection connection, String contact, String password, int stage) {
        String query = "SELECT * FROM users WHERE (user_contact = ? OR user_id = ?) AND user_password = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, contact);
            preparedStatement.setString(2, contact);
            preparedStatement.setString(3, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (stage == 0) {
                    return true;
                } else {
                    AccountManager.entryPoint(connection, new Scanner(System.in), resultSet);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean checkAccountByEmail(Connection connection, String contact) {
        String query = "SELECT * FROM users WHERE user_contact = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, contact);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
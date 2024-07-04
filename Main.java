import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/bank_manager";
    private static final String username = "root";
    private static final String password = "P@ssword";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            while (true) {
                String responseName, responseContact, responsePassword;
                User user = new User();
                System.out.println("Shishir Bank Management\n");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Quit");
                System.out.print("Enter: ");
                int response = getUserIntInput();
                scanner.nextLine();
                switch (response) {
                    case 1:
                        System.out.println("**********Create new Account**********\n");
                        System.out.print("Enter Your full Name: ");
                        responseName = scanner.nextLine();
                        System.out.print("Enter Your Email: ");
                        responseContact = scanner.nextLine();
                        System.out.print("Enter Your password: ");
                        responsePassword = scanner.nextLine();
                        user.setRegister(connection, scanner, responseName, responseContact, responsePassword);
                        break;
                    case 2:
                        System.out.println("********************Welcome Back!****************");
                        System.out.print("\nEnter Your Account Id / Email: ");
                        String userId = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        String pass = scanner.nextLine();
                        user.setLogin(connection, scanner, userId, pass);
                        break;
                    case 3:
                        toQuit();
                        return;
                    default:
                        System.out.println("Invalid choice!!!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void toQuit() throws InterruptedException {
        int i = 5;
        System.out.println();
        System.out.print("Exiting the system");
        while (i > 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
    }

    private static int getUserIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

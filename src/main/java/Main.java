import login.Login;
import queryProcessor.QueryProcessor;
import queryValidator.QueryValidator;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        String userInput;
        String username;
        String password;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your username: ");
        username = input.nextLine();
        System.out.println("Enter your password: ");
        password = input.nextLine();
        // Authenticate user
        Login login = new Login();
        login.loadUser();
        if (login.authenticateUser(username, password)) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login unsuccessful");
            return;
        }
        // Accept a query
        System.out.println("Enter your query: ");
        userInput = input.nextLine();
        System.out.println(userInput);
        // Create instances for each module
        QueryValidator validator = new QueryValidator();
        QueryProcessor processor = new QueryProcessor();
    }
}

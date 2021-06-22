import queryProcessor.QueryProcessor;
import queryValidator.QueryValidator;

import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        String userInput;
        System.out.println("Hello World!");
        Scanner input = new Scanner(System.in);
        // Authenticate user
        // Accept a query
        userInput = input.nextLine();
        System.out.println(userInput);
        // Create instances for each module
        QueryValidator validator = new QueryValidator();
        QueryProcessor processor = new QueryProcessor();
    }
}

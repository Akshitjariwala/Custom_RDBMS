import queryValidator.QueryValidator;
import userAuthentication.Login;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    
    public static QueryValidator queryValidator;
    
    public static void main (String[] args) throws IOException {
        Boolean menuFlag = false;
        
        do {
            System.out.println("\n\n----------------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\tDatabase Server\t\t\t\t\t");
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("1. Login.");
            System.out.println("2. Create User.");
            System.out.println("\n Select 1 to login to an existing account and 2 to create a new account.\n");
            boolean loopMenu = true;
            while (loopMenu) {
                loopMenu = false;
                System.out.print("> ");
                Scanner input = new Scanner(System.in);
                String username;
                String password;
                String question;
                String answer;
                String selection = input.nextLine().trim().toLowerCase();
                switch (selection) {
                    case "exit":
                        return;
                    case "1":
                        // Get user authentication input
                        System.out.println("Enter your username: ");
                        username = input.nextLine();
                        System.out.println("Enter your password: ");
                        password = input.nextLine();
                        // Authenticate user
                        boolean loginResults = Login.authenticateUser(username, password);
                        if (!loginResults) {
                            loopMenu = true;
                            break;
                        }
                        System.out.print("Answer: ");
                        answer = input.nextLine();
                        boolean securityQuestionResults = Login.verifySecurityQuestion(answer);
                        if (!securityQuestionResults) {
                            loopMenu = true;
                            break;
                        }
                        System.out.println("Successfully authenticated user");
                        queryValidator = new QueryValidator();
                        queryValidator.QueryValidator();
                        menuFlag = true;
                        break;
                    case "2":
                        System.out.println("Enter your username: ");
                        username = input.nextLine();
                        System.out.println("Enter your password: ");
                        password = input.nextLine();
                        System.out.println("Enter your security question: ");
                        question = input.nextLine();
                        System.out.println("Enter your security question answer: ");
                        answer = input.nextLine();
                        boolean createUserInputIsValid = !username.isBlank() || !password.isBlank() || !question.isBlank() || !answer.isBlank();
                        if (!createUserInputIsValid) {
                            System.out.println("Invalid info");
                            break;
                        }
                        Login.createUser(username, password, question, answer);
                        menuFlag = true;
                        break;
                    default:
                        System.out.println("Invalid input");
                        loopMenu = true;
                        break;
                }
            }
        }while(menuFlag);
    }
}

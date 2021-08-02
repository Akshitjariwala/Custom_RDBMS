package userAuthentication;
import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Login {
    private static final String secret = "BC62AB92D2";
    private static final String workingDir = System.getProperty("user.dir");
    private static final Properties currentUser = new Properties();
    private static final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    public static void renderUI() {
        String userInput;
        String username;
        String password;
        String answer;
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tDatabase Server\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("1. Login.");
        System.out.println("2. Create User.");
        System.out.println("\n Select 1 to login to an existing account and 2 to create a new account.\n");
        Scanner input = new Scanner(System.in);
        try {
            String selection = inputReader.readLine();
            if (selection.equals("1")) {
                // Get user authentication input
                System.out.println("Enter your username: ");
                username = input.nextLine();
                System.out.println("Enter your password: ");
                password = input.nextLine();
                // Authenticate user
            } else if (selection.equals("2")) {

            } else {
                System.out.println("Invalid input");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean authenticateUser(String username, String password, String answer) {
        try {
            currentUser.load(new FileInputStream(workingDir + "/appdata/users/" + username + ".user"));
            String correctUsername = EncryptionDecryptionUtil.decrypt(secret, currentUser.getProperty("username"));
            String correctPassword = EncryptionDecryptionUtil.decrypt(secret, currentUser.getProperty("password"));
            boolean passwordIsCorrect = (username.equals(correctUsername) && password.equals(correctPassword));
            boolean answerIsCorrect = answer.equals(currentUser.getProperty("answer"));
            if (passwordIsCorrect && answerIsCorrect) {
                System.out.println("Successfully authenticated " + username);
                return true;
            } else {
                if (!passwordIsCorrect) {
                    System.out.println("Password or username is incorrect");
                }
                if (!answerIsCorrect) {
                    System.out.println("Answer is incorrect");
                }
                return false;
            }
        } catch (IOException e) {
            System.out.println("User not found");
        }
        return false;
    }

    public static void createUser(String username, String password, String question, String answer) {
        Properties user = new Properties();
        user.put("username", EncryptionDecryptionUtil.encrypt(secret, username));
        user.put("password", EncryptionDecryptionUtil.encrypt(secret, password));
        user.put("question", question);
        user.put("answer", answer);
        String filePath = workingDir + "/appdata/users/" + username + ".user";
        if (new File(filePath).exists()) {
            System.out.println("User \"" + username + "\" already exists");
            return;
        }
        try {
            FileOutputStream fr = new FileOutputStream(filePath);
            user.store(fr, "UserAccount");
            System.out.println("Successfully created user \"" + username + "\"");
            fr.close();
        } catch (IOException e) {
            System.out.println("Failed to create user");
        }
    }
}

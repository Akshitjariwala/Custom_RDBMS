package userAuthentication;
import java.io.*;
import java.util.Properties;

public class Login {
    private static final String secret = "BC62AB92D2";
    private static final String workingDir = System.getProperty("user.dir");
    private static final Properties currentUser = new Properties();

    public static boolean authenticateUser(String username, String password) {
        try {
            currentUser.load(new FileInputStream(workingDir + "/appdata/users/" + username + ".user"));
            String correctUsername = currentUser.getProperty("username");
            String correctPassword = EncryptionDecryptionUtil.decrypt(secret, currentUser.getProperty("password"));
            boolean passwordIsCorrect = (username.equals(correctUsername) && password.equals(correctPassword));
            if (passwordIsCorrect) {
                System.out.println("Password and username ok");
                System.out.println("Security question: " + currentUser.getProperty("question"));
                return true;
            } else {
                System.out.println("Password or username is incorrect");
                return false;
            }
        } catch (IOException e) {
            System.out.println("User not found");
        }
        return false;
    }

    public static boolean verifySecurityQuestion(String answer) {
        if (currentUser.isEmpty() || !currentUser.containsKey("question")) {
            System.out.println("Authenticate user first");
            return false;
        }
        boolean answerIsCorrect = answer.equals(EncryptionDecryptionUtil.decrypt(secret, currentUser.getProperty("answer")));
        if (!answerIsCorrect) {
            System.out.println("Answer is incorrect");
            return false;
        } else {
            System.out.println("Answer is correct");
            return true;
        }
    }

    public static void createUser(String username, String password, String question, String answer) {
        Properties user = new Properties();
        user.put("username", username);
        user.put("password", EncryptionDecryptionUtil.encrypt(secret, password));
        user.put("question", question);
        user.put("answer", EncryptionDecryptionUtil.encrypt(secret, answer));
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

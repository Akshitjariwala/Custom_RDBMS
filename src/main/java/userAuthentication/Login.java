package userAuthentication;
import java.io.*;
import java.util.Properties;

public class Login {
    private final Properties user = new Properties();
    private final String username;
    private final String secret;

    public Login(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public Login() {
        this.username = "default";
        this.secret = "default";
    }

    public void loadUser() {
        try {
            user.load(new FileInputStream(("appdata/users/" + username + ".properties")));
        } catch (IOException e) {
            throw new RuntimeException("User not found", e);
        }
    }

    public void createUser(String username, String password, String question, String answer) {
        user.put("username", EncryptionDecryptionUtil.encrypt(secret, username));
        user.put("password", EncryptionDecryptionUtil.encrypt(secret, password));
        user.put("question", question);
        user.put("answer", answer);
        try {
            FileOutputStream fr = new FileOutputStream("appdata/users/" + username +".properties");
            user.store(fr, "UserAccount");
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    public boolean authenticateUser(String username, String password) {
        String correctUsername = EncryptionDecryptionUtil.decrypt(secret, user.getProperty("username"));
        String correctPassword = EncryptionDecryptionUtil.decrypt(secret, user.getProperty("password"));
        return (username.equals(correctUsername) && password.equals(correctPassword));
    }

    public String getSecurityQuestion() {
        return user.getProperty("question");
    }

    public boolean validateSecurityQuestion(String answer) {
        String correctAnswer = user.getProperty("answer");
        return answer.equals(correctAnswer);
    }
}

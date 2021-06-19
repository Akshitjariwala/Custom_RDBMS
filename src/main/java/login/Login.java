package login;

import java.io.*;
import java.util.Properties;

public class Login {
    Properties users = new Properties();

    public Login() throws IOException {
        users.load(new FileInputStream(("resources/login.properties")));
    }

    public boolean authenticateUser(String username, String password) {
        String correctUsername = "";
        String correctPassword = "";
        return (username.equals(correctUsername) && password.equals(correctPassword));
    }
}

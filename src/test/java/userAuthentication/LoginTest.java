package userAuthentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadUser() {
        /*Login login = new Login();
        login.loadUser();*/
    }

    @Test
    void renderUI() {
    }

    @Test
    void createUser() {
        String username = "user1";
        String password = "pass";
        String question = "what is the answer?";
        String answer = "answer";
        /*Login.createUser(username, password, question, answer);*/
    }

    @Test
    void authenticateUser() {
       /* Login login = new Login();
        login.loadUser();
        assertTrue(login.authenticateUser("user", "pass"));*/
    }

    @Test
    void getSecurityQuestion() {
    }

    @Test
    void validateSecurityQuestion() {
    }
}
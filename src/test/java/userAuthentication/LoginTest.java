package userAuthentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadUser() {

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
        Login.createUser(username, password, question, answer);
    }

    @Test
    void authenticateUser() {
        String username = "user1";
        String password = "pass";
        String answer = "answer";
        assertTrue(Login.authenticateUser(username, password));
        assertTrue(Login.verifySecurityQuestion(answer));
    }

    @Test
    void getSecurityQuestion() {
    }

    @Test
    void validateSecurityQuestion() {
    }
}
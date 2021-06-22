package login;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
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
            user.load(new FileInputStream(("src/main/resources/" + username +".properties")));
        } catch (IOException e) {
            throw new RuntimeException("User not found", e);
        }
    }

    public void createUser(String username, String password, String question, String answer) {
        user.put("username", EncryptionDecryptionUtil.encrypt(secret, username));
        user.put("password", EncryptionDecryptionUtil.encrypt(secret, password));
        user.put("question", question);
        user.put("answer", answer);
;       try {
            FileOutputStream fr = new FileOutputStream("src/main/resources/" + username +".properties");
            user.store(fr, "Properties");
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a user", e);
        }
    }

    public boolean authenticateUser(String username, String password) {
        String correctUsername = EncryptionDecryptionUtil.decrypt(secret, user.getProperty("username"));
        String correctPassword = EncryptionDecryptionUtil.decrypt(secret, user.getProperty("password"));
        return (username.equals(correctUsername) && password.equals(correctPassword));
    }

    public boolean validateSecurityQuestion(String answer) {

        return false;
    }

    // Adapted from https://stackoverflow.com/questions/10494764/input-length-must-be-multiple-of-16-when-decrypting-with-padded-cipher
    public static class EncryptionDecryptionUtil {

        public static String encrypt(final String secret, final String data) {

            byte[] decodedKey = Base64.getDecoder().decode(secret);

            try {
                Cipher cipher = Cipher.getInstance("AES");
                SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
                cipher.init(Cipher.ENCRYPT_MODE, originalKey);
                byte[] cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(cipherText);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while encrypting data", e);
            }

        }

        public static String decrypt(final String secret, final String encryptedString) {
            byte[] decodedKey = Base64.getDecoder().decode(secret);

            try {
                Cipher cipher = Cipher.getInstance("AES");
                // rebuild key using SecretKeySpec
                SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
                cipher.init(Cipher.DECRYPT_MODE, originalKey);
                byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
                return new String(cipherText);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while decrypting data", e);
            }
        }
    }
}

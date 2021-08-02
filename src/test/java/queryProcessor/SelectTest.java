package queryProcessor;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    @Test
    void execute() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tSelect Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // SELECT user_name, user_email, user_contact FROM user_data WHERE user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("columns", Arrays.asList("user_name", "user_email", "user_contact"));
        validationTokens.put("isValid", true);
        validationTokens.put("where", Arrays.asList("user_email=alex.mark@gmail.com", "user_name=alex", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        Select.execute(validationTokens);
    }
}
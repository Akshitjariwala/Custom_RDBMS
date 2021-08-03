package queryProcessor;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    @Test
    void load() throws FileNotFoundException {
        QueryProcessor.loadTableToArray("appdata/database/database1/user_data.txt");
    }

    @Test
    void execute() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tSelect Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // SELECT user_name, user_email, user_contact FROM user_data WHERE user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("columns", Arrays.asList("user_name", "user_email", "user_contact"));
        validationTokens.put("isValid", true);
        validationTokens.put("where", Arrays.asList("user_name=Mukesh", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        // Select.execute(validationTokens);
    }

    @Test
    void selectTest() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tSelect Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // select user_id,user_name,user_email from user_data where user_id = 1 and user_name = Mukesh
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("columns", Arrays.asList("user_id", "user_name", "user_email"));
        validationTokens.put("isValid", true);
        validationTokens.put("where", Arrays.asList("user_id = 1", "user_name = Mukesh"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        Select.execute(validationTokens);
    }
}
package queryProcessor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTest {

    @Test
    void execute() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tDelete Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // DELETE FROM user_data WHERE user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("columns", Arrays.asList(""));
        validationTokens.put("isValid", true);
        validationTokens.put("where", Arrays.asList("user_email=alex.mark@gmail.com", "user_name=alex", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        Delete.execute(validationTokens);
    }
}
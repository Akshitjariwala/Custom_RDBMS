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
        // DELETE FROM user_data WHERE user_name=alex AND user_contact=5566223311
        // DV: {whereArray=[user_name=alex, user_contact=5566223311], isValid=false, tableName=user_data}
        Map<String, Object> validationTokens = new HashMap<>();
        // validationTokens.put("columns", Arrays.asList(""));
        validationTokens.put("isValid", true);
        validationTokens.put("whereArray", Arrays.asList("user_name=alex", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        System.out.println(validationTokens);
        // Delete.execute(validationTokens);
        // QV: {whereArray=[user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311], databaseName=database1, isValid=false, tableName=user_data}
        // TS: {whereArray=[user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311], databaseName=database1, columns=[], isValid=true, tableName=user_data}
    }
}
package queryProcessor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {

    @Test
    void main() {
    }

    @Test
    void performUpdate() {
    }

    @Test
    void execute() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tUpdate Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // UPDATE user_data set user_name=UpdatedMukesh,user_contact=Updated5566223311 where user_name=Mukesh AND user_contact=5566223311
        // UPDATE FROM user_data WHERE user_email=alex.mark@gmail.com AND user_name=alex AND user_contact=5566223311
        // {setColumns=[user_name=newalex, user_contact=new5566223311], whereColumnList=[user_contact, 5566223311], setColumnList=[user_contact, new5566223311], databaseName=database1, isValid=true, whereList=[user_name=alex, user_contact=5566223311], tableName=user_data}
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("setColumns", Arrays.asList("user_name=UpdatedMukesh", "user_contact=new5566223311"));
        validationTokens.put("whereList", Arrays.asList("user_name=Mukesh", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        System.out.println(validationTokens);
        Update.execute(validationTokens);
    }
}
package queryValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class dropValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    
    public static Map<String,Object> validateDrop(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        String dropPattern = "[D-d][R-r][O-o][P-p]\\s+[T-t][A-a][B-b][L-l][E-e]\\s+[A-Za-z0-9_]+";
        Map<String,Object> tokens = new HashMap<>();
        
        if(query.matches(dropPattern)) {
            String tableName = queryTokens[2];
            if(tableValidationMethods.checkTable(tableName,databaseName)) { //Perform semantic analysis. Pass #TableName.
                tokens.put("tableToDrop",tableName);
                isValid = true;
            } else {
                System.out.println("ERROR: Table Does Not Exists In The Database.");
                isValid = false;
            }
        } else {
            System.out.println("ERROR: Incorrect DROP syntax.");
        }
        
        if(isValid) {
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered DROP query is valid.");
        } else {
            tokens.put("isValid",false);
        }
        
        //System.out.println(tokens);
        return tokens;
    }
}

package queryValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class deleteValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    
    public static Map<String,Object> validateDelete(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        String deletePattern = "[D-d][E-e][L-l][E-e][T-t][E-e]\\s+[F-f][R-r][O-o][M-m]\\s+[A-Za-z0-9_]+\\s*";
        String whereSubString = "";
        String deleteSubString = "";
        int whereLength = 5;
        Map<String,Object> tokens = new HashMap<>();
        String tableName = queryTokens[2];
        tokens.put("tableName",tableName);
        int indexWhere = query.toUpperCase().indexOf("WHERE");
        
        // check if contains where clause and table name.
        if(tableValidationMethods.containsWhere(queryTokens)) {
            if(!queryTokens[2].toUpperCase().equals("WHERE")){
                deleteSubString = query.substring(0,indexWhere);
                if(deleteSubString.matches(deletePattern)) {
                    whereSubString = query.substring(indexWhere+whereLength+1);
                    String[] whereArray = whereSubString.replaceAll("\"","").split("\\s+(?i)and\\s+");
                    tokens.put("whereArray",tableValidationMethods.createListFromArray(whereArray));
                    for(int i=0;i<whereArray.length;i++){
                        if(tableValidationMethods.validateWhereClause(whereArray[i])){
                            if(tableValidationMethods.checkTable(tableName,databaseName)){ // Perform semantic analysis on tableName.
                                // Convert where clauses into array of columns
                                String[] columnsArray = whereArray[i].replaceAll("\"","").split("\\s*=\\s*");
                                tokens.put("columnsArray",tableValidationMethods.createListFromArray(columnsArray));
                                for(int j=0;j<columnsArray.length;j++){
                                    String[] list = {columnsArray[0]};
                                    if(tableValidationMethods.checkTableAndColumn(tableName,list,databaseName)){ // Perform semantic analysis on columns. Pass #columnsArray.
                                        isValid = true;
                                    } else {
                                        isValid = false;
                                        System.out.println("ERROR: Column "+columnsArray[j]+" is not present in table "+tableName);
                                        break;
                                    }
                                    j++;
                                }
                            } else {
                                isValid = false;
                                System.out.println("ERROR: Table Does Not Exists In The Database.");
                                break;
                            }
                        } else {
                            System.out.println("ERROR: Error In Where Clause");
                            isValid = false;
                            break;
                        }
                    }
                } else {
                    System.out.println("ERROR: Invalid DELETE Query Syntax");
                }
            } else {
                System.out.println("ERROR: No Table Name Provided. Please Provide Table Name In The Query.");
                isValid = false;
            }
            
        } else {
            if(queryTokens.length == 3){
                isValid = true;
            } else {
                isValid = false;
                System.out.println("ERROR: Incorrect Syntax In Where Clause.");
            }
        }
        
        if(isValid){
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered DELETE query is valid.");
        } else {
            tokens.put("isValid",false);
        }
        
        //System.out.println(tokens);
        return tokens;
    }
}

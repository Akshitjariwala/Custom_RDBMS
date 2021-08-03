package queryValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class insertValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    
    public static Map<String,Object> validateInsert(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        int indexOfColumns = query.toUpperCase().indexOf("(");
        int indexOfColumnsEnd = query.toUpperCase().indexOf(")");
        int lengthOfValues = 7;
        String columnSubString = "";
        String valuesSubString = "";
        int indexOfValues1 = query.toUpperCase().indexOf("VALUES");
        int indexOfValues = query.toUpperCase().indexOf("(",indexOfValues1);
        int indexOfValuesEnd = query.toUpperCase().indexOf(")",indexOfValues);
        String[] tempArray = new String[queryTokens.length];
        String insertPattern = "[I-i][N-n][S-s][E-e][R-r][T-t]\\s+[I-i][N-n][T-t][O-o]\\s+[A-Za-z0-9_]+\\s*\\([A-Za-z0-9,_]+\\)\\s*[V-v][A-a][L-l][U-u][E-e][S-s]\\s*\\([A-Za-z0-9,_.@\\s\"]+\\)";
        int indexOfInto = query.toUpperCase().indexOf("INTO");
        Map<String,Object> tokens = new HashMap<>();
        String tableName = query.substring(indexOfInto+4,query.indexOf("(")).trim();
        tokens.put("tableName",tableName);
        // To upper case
        for(int j=0;j<queryTokens.length;j++){
            tempArray[j] =  queryTokens[j].toUpperCase();
        }
        
        if(query.matches(insertPattern)){
            columnSubString = query.substring(indexOfColumns+1,indexOfColumnsEnd);
            columnSubString = columnSubString.trim();
            String[] columnsArray = columnSubString.split("\\s*,\\s*");
            tokens.put("columnNames",tableValidationMethods.createListFromArray(columnsArray));
            valuesSubString = query.substring(indexOfValues+1,indexOfValuesEnd);
            valuesSubString = valuesSubString.trim();
            String[] valuesArray = valuesSubString.replaceAll("\"","").split("\\s*,\\s*");
            tokens.put("columnValues",tableValidationMethods.createListFromArray(valuesArray));
            
            if(columnsArray.length == valuesArray.length){
                for(int i=0;i<columnsArray.length;i++){
                    if(!(columnsArray[i].equals("")) && columnsArray[i].matches("[A-Za-z0-9_]+") && !(valuesArray[i].equals("")) && valuesArray[i].matches("[A-Za-z0-9,_.@\\s\"]+")){
                        if(tableValidationMethods.checkTableAndColumn(tableName,columnsArray,databaseName)) { //perform semantic analysis to check if columns exits and belongs to table. Pass #tableName and columnArray.
                            isValid = true;
                        } else {
                            isValid = false;
                            System.out.println("ERROR: Error in SQL syntax. Columns Does Not Match With Table "+tableName);
                            break;
                        }
                    } else {
                        isValid = false;
                        System.out.println("ERROR: Error in SQL syntax. Invalid Column Name Or Value.");
                        break;
                    }
                }
            } else {
                System.out.println("ERROR: Error In SQL Syntax. Columns In Query Does Not Match.");
            }
        }
        else {
            System.out.println("ERROR: Invalid INSERT Query Syntax.");
        }
        
        if(isValid){
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered INSERT Query Is Valid.");
        } else {
            tokens.put("isValid",false);
        }
        
        //System.out.println(tokens);
        return tokens;
    }
    
}

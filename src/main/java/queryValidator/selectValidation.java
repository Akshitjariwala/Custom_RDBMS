package queryValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selectValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    
    public static Map<String,Object> validateSelect(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        boolean mustClauseFlag = false;
        boolean tableColumnFlag = false;
        ArrayList<String> whereClauseArray = new ArrayList<String>();
        String optionalClause = "WHERE";
        String mustClause = "FROM";
        String tableName = "";
        int index=0;
        Map<String,Object> tokens = new HashMap<>();
        List<Object> tokenList = new ArrayList<>();
        
        // Validate the must clause.
        for (int i=0;i<queryTokens.length;i++){
            if(queryTokens[i].toUpperCase().equals(mustClause)) {
                if(i != 1){
                    mustClauseFlag = true;
                    index = i;
                }
            }
        }
        
        // Check for table name syntax. Check if Table exists in the database. Check if columns exists in the database.
        if(mustClauseFlag) {
            if(index < queryTokens.length-1) {
                if(!(optionalClause.equals(queryTokens[index+1].toUpperCase()))) {
                    tableName = queryTokens[index+1];
                    tokens.put("tableName",tableName);
                    if(tableValidationMethods.checkTable(tableName,databaseName)) {   // Perform semantic analysis on Table Name. Check if table exists in the system.
                        if(queryTokens[1].equals("*")){
                            tableColumnFlag = true;
                        } else {
                            int indexOfFrom = query.toUpperCase().indexOf("FROM");
                            String colSubString = query.substring(6,indexOfFrom);
                            colSubString = colSubString.replaceAll("\\s*","");
                            if(colSubString.charAt(colSubString.length() - 1) != ','){
                                String[] colString = colSubString.split("\\s*,\\s*");
                                for(int i=0;i<colString.length;i++){
                                    if(!(colString[i].equals("")) && colString[i].matches("[A-Za-z0-9_]+")){
                                        if(tableValidationMethods.checkTableAndColumn(tableName,colString,databaseName)){ //semantic analysis for columns
                                            List<String> tempList = tableValidationMethods.createListFromArray(colString);
                                            tokens.put("columns",tempList);
                                            tableColumnFlag = true;
                                        } else {
                                            System.out.println("ERROR: Invalid Column Names. Table '"+tableName+"' Does Not Contain Mentioned Columns.");
                                            tableColumnFlag = false;
                                            break;
                                        }
                                    } else {
                                        tableColumnFlag = false;
                                        System.out.println("ERROR: Incorrect syntax in column names.");
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("ERROR: Incorrect syntax in Column names.");
                            }
                        }
                    } else {
                        System.out.println("ERROR: No such table named "+tableName+" present in the database.");
                    }
                } else {
                    System.out.println("ERROR: No Table name provided. Please provide Table name in the query.");
                }
            } else {
                System.out.println("ERROR: No Table name provided.Enter Query Again.");
            }
        } else {
            System.out.println("ERROR: Invalid Query Syntax. Required clauses not provided. Please Enter Query Again.");
        }
        
        if(tableColumnFlag) {
            if((index + 1) != queryTokens.length-1){
                if (queryTokens[index + 2].toUpperCase().equals("WHERE")) {
                    // Convert into subarray of where.
                    for (int i=index + 3;i<queryTokens.length;i++){
                        whereClauseArray.add(queryTokens[i].replaceAll("\"",""));
                    }
                    tokens.put("where",whereClauseArray);
                    int indexOfWhere = query.toUpperCase().indexOf("WHERE");
                    int indexOfNextWord = indexOfWhere + 5;
                    String subString = query.substring(indexOfNextWord+1,query.length());
                    
                    if(whereClauseArray.contains("AND") || whereClauseArray.contains("and")) {
                        
                        String[] subArray = subString.split("\\s+(?i)and\\s+");
                        
                        for(int j=0;j<subArray.length;j++){
                            subArray[j] = subArray[j].trim();
                            if(tableValidationMethods.validateWhereClause(subArray[j])){
                                isValid = true;
                            } else {
                                System.out.println("ERROR: Invalid SQL Syntax. Error in WHERE clause.");
                                isValid = false;
                                break;
                            }
                        }
                    } else {
                        if(tableValidationMethods.validateWhereClause(subString.trim())){
                            isValid = true;
                        } else {
                            System.out.println("ERROR: Incorrect syntax in where clause.");
                        }
                    }
                } else {
                    isValid = false;
                }
            } else {
                isValid = true;
            }
        }
        if(isValid) {
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered SELECT query is VALID.");
        } else {
            tokens.put("isValid",false);
            System.out.println("");
        }
        
        System.out.println(tokens);
        return tokens;
    }
}

package queryValidator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class alterValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    public static String[] typeArray = {"INT","FLOAT","VARCHAR"};
    public static Map<String,Object> validateAlter(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        String alterPattern = "[A-a][L-l][T-t][E-e][R-r]\\s+[T-t][A-a][B-b][L-l][E-e]\\s+[A-Za-z0-9_]+";
        String[] alterClauses = {"ADD","MODIFY","DROP","CHANGE","RENAME_TO"};
        String alterSyntax = "[A-Za-z0-9_]+\\s+[A-Za-z0-9]+";
        String alterClause="";
        String alterQuery = "";
        Map<String,Object> tokens = new HashMap<>();
        String tableName =  queryTokens[2];
        tokens.put("tableName",tableName);
        boolean syntaxFlag = false;
        boolean alterFlag = false;
        boolean table = false;
        int alterLength=0;
        int querylength = 4;
        String columnName ="";
        String columnDatatype = "";
        String newColumnName = "";
        
        if(Arrays.asList(alterClauses).contains(queryTokens[3].toUpperCase())){
            alterClause = queryTokens[3].toUpperCase();
            alterLength = alterClause.length();
            if(!(queryTokens.length == 4)) {
                if(!Arrays.asList(alterClauses).contains(queryTokens[4].toUpperCase())){
                    alterQuery = query.substring(0,query.toUpperCase().indexOf(alterClause)).trim();
                    if(alterQuery.matches(alterPattern)){
                        alterFlag = true;
                    } else {
                        System.out.println("ERROR: Incorrect SQL syntax. Please enter query again.");
                    }
                } else {
                    System.out.println("ERROR: Invalid column name. Please provide proper column name.");
                }
            } else {
                System.out.println("ERROR: Incorrect syntax in "+alterClause+" clause.");
            }
        } else {
            System.out.println("ERROR: Incorrect ALTER syntax. Please enter query again.");
        }
        
        
        
        if(alterFlag){
            alterQuery = query.substring(query.toUpperCase().indexOf(alterClause)+alterLength,query.length()).trim();
            if(alterClause.equals("DROP") || alterClause.equals("RENAME_TO")) {
                if(queryTokens.length == querylength+1){
                    if(alterClause.equals("RENAME_TO")) {
                        tokens.put("type","RENAME_TO");
                        String newTableName = queryTokens[querylength];
                        tokens.put("newTableName",newTableName);
                        if(newTableName.matches("[A-Za-z0-9_]+")){
                            table = true;
                        } else {
                            System.out.println("ERROR: Invalid table name. Table Name can only contain alpha numeric values and '_' ");
                        }
                    } else {
                        tokens.put("type","DROP");
                        table = true;
                        columnName = queryTokens[querylength];
                        tokens.put("columnToDrop",columnName);
                    }
                } else {
                    System.out.println("ERROR: Incorrect SQL syntax in "+alterClause+" .Please enter query again.");
                }
            }
            
            if(alterClause.equals("ADD") || alterClause.equals("MODIFY")) {
                if(alterQuery.matches(alterSyntax)){
                    switch(alterClause) {
                        case "ADD"    : columnName = queryTokens[querylength]; columnDatatype = queryTokens[querylength+1];
                            tokens.put("columnName",columnName);
                            tokens.put("newColumnDatatype",columnDatatype);
                            if(Arrays.asList(typeArray).contains(columnDatatype.toUpperCase()))
                            { table = true; tokens.put("type","ADD"); } else {
                                System.out.println("ERROR: Invalid datatype.");
                            } break;
                        case "MODIFY" : columnName = queryTokens[querylength]; columnDatatype = queryTokens[querylength+1];
                            tokens.put("columnName",columnName);
                            tokens.put("newColumnDatatype",columnDatatype);
                            if(Arrays.asList(typeArray).contains(columnDatatype.toUpperCase()))
                            { table = true; tokens.put("type","MODIFY"); } else {
                                System.out.println("ERROR: Invalid Data Type. Supported datatypes are (INT,VARCHAR,FLOAT)");
                            } break;
                    }
                } else {
                    System.out.println("ERROR: InCorrect syntax in "+alterClause+" ");
                }
            }
            
            if(alterClause.equals("CHANGE")) {
                tokens.put("type","CHANGE");
                if(querylength+3 == queryTokens.length) {
                    columnName = queryTokens[querylength]; newColumnName = queryTokens[querylength+1];
                    tokens.put("newColumnName",newColumnName);
                    columnDatatype = queryTokens[querylength+2];
                    if (Arrays.asList(typeArray).contains(columnDatatype.toUpperCase())) {
                        tokens.put("newDataType",columnDatatype.toUpperCase());
                        if(newColumnName.matches("[A-Za-z0-9_]+")) {
                            table = true;
                        } else {
                            System.out.println("ERROR: Invalid New Column Name.");
                        }
                    } else {
                        System.out.println("ERROR: Invalid Data type. Supported datatypes are (INT,VARCHAR,FLOAT)");
                    }
                } else {
                    System.out.println("ERROR: Incorrect syntax in "+alterClause+" clause.");
                }
            }
        }
        
        String[] columnList = new String[1];
        tokens.put("originalColumnList",tableValidationMethods.createListFromArray(columnList));
        columnList[0] = columnName;
        
        // Perform semantic analysis. Pass #TableName.
        if(table) {
            if(tableValidationMethods.checkTable(tableName,databaseName)){
                if(!(alterClause.toUpperCase().equals("RENAME_TO"))){
                    if(tableValidationMethods.checkTableAndColumn(tableName,columnList,databaseName)) {
                        isValid = true;
                    } else {
                        System.out.println("ERROR: Columns Does Not Exists In The Database.");
                        isValid = false;
                    }
                } else {
                    isValid = true;
                }
            } else {
                System.out.println("ERROR: Table Does Not Exists In The Database.");
            }
        }
        
        if(isValid) {
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered ALTER query is valid.");
        } else {
            tokens.put("isValid",false);
        }
        
        //System.out.println(tokens);
        return tokens;
    }
}

package queryValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class createValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    public static String tableNamePattern = "[A-Za-z0-9_]+";
    public static String[] typeArray = {"INT","FLOAT","VARCHAR"};
    public static ArrayList<String> columnList = new ArrayList<>();
    
    public static Map<String,Object> validateCreate(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        String tablePattern = "[C-c][R-r][E-e][A-a][T-t][E-e]\\s+[T-t][A-a][B-b][L-l][E-e]\\s+[A-Za-z0-9_]+\\s*\\(";
        int createDefaultLength = 3;
        String columnDefinitionPattern = "[A-Za-z0-9_]";
        int queryTypeIndex = 1;
        String tableName;
        int columnIndex = query.indexOf("(")+1;
        int columnEndIndex = query.length()-1;
        boolean createValid = false;
        boolean validLength = false;
        String columnsSubString = "";
        Map<String,Object> tokens = new HashMap<>();
        
        if(queryTokens[queryTypeIndex].toUpperCase().equals("TABLE")) {
            tableName = queryTokens[2];
            if(tableName.contains("(")){
                tableName = tableName.substring(0,tableName.indexOf("(")).trim();
            } else {
                tableName = tableName.trim();
            }
            tokens.put("tableName",tableName);
            if(!tableValidationMethods.checkTable(tableName,databaseName)) { //Perform semantic analysis. Pass #TableName.
                String craeteSubQuery = query.substring(0,columnIndex).trim();
                if(craeteSubQuery.matches(tablePattern) && Character.compare(query.charAt(query.length()-1),')') == 0)  {
                    createValid = true;
                } else {
                    System.out.println("ERROR: Incorrect CREATE Statement.");
                }
            } else {
                System.out.println("ERROR: TABLE Already exist in the database.");
            }
        } else {
            System.out.println("ERROR: CREATE query type not provided.");
        }
        
        if(createValid){
            columnsSubString = query.substring(columnIndex,columnEndIndex).trim();
            if(!(columnsSubString.charAt(columnsSubString.length()-1) == ',')){
                String[] creationArray = columnsSubString.split("\\s*,\\s*");
                tokens.put("columnArray",tableValidationMethods.createListFromArray(creationArray));
                for(int i=0;i<creationArray.length;i++){
                    if(!creationArray[i].matches("\\s*")) {
                        if(validateColumnDefinition(creationArray[i].trim(),databaseName)){
                            isValid = true;
                            continue;
                        } else {
                            isValid = false;
                            break;
                        }
                    } else {
                        isValid = false;
                        System.out.println("ERROR: Incorrect Syntax in Table Definition.");
                        break;
                    }
                }
                columnList.clear();
            } else {
                System.out.println("ERROR: NULL Column definition.");
            }
        }
        
        if(isValid) {
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered CREATE query is valid.");
        } else{
            tokens.put("isValid",false);
        }
        
        System.out.println(tokens);
        return tokens;
    }
    
    public static boolean validateColumnDefinition(String coumnDef,String databaseName) throws IOException {
        boolean bool =  false;
        String columnConstraint;
        String columnName;
        String constraintName="";
        String[] arrayCol = coumnDef.split("\\s+");
        String pkPattern = "[P-p][R-r][I-i][M-m][A-a][R-r][Y-y][_][K-k][E-e][Y-y]\\s*\\([A-Za-z0-9_]+\\)[,]*";
        String fkPattern = "[F-f][O-o][R-r][E-e][I-i][G-g][N-n][_][K-k][E-e][Y-y]\\s+\\([A-Za-z0-9_]+\\)\\s+[R-r][E-e][F-f][E-e][R-r][E-e][N-n][C-c][E-e][S-s]\\s+[A-Za-z0-9_]+\\([A-Za-z0-9_]+\\)[,]*";
        String fkPattern2 = "[C-c][O-o][N-n][S-s][T-t][R-r][A-a][I-i][N-n][T-t]\\s+[A-Za-z0-9_]+\\s+[F-f][O-o][R-r][E-e][I-i][G-g][N-n][_][K-k][E-e][Y-y]\\s+\\([A-Za-z0-9_]+\\)\\s+[R-r][E-e][F-f][E-e][R-r][E-e][N-n][C-c][E-e][S-s]\\s+[A-Za-z0-9_]+\\([A-Za-z0-9_]+\\)[,]*";
        columnName = arrayCol[0];
        columnConstraint = arrayCol[1];
        
        columnList.add(columnName);
        
        if (arrayCol.length > 2) {
            constraintName = arrayCol[2];
        }
        
        if(columnName.matches(tableNamePattern) && !columnName.toUpperCase().equals("PRIMARY_KEY") && !columnName.toUpperCase().equals("FOREIGN_KEY") && !columnName.toUpperCase().equals("CONSTRAINT")) {
            if(Arrays.asList(typeArray).contains(columnConstraint.toUpperCase())) {
                if(arrayCol.length==3){
                    if(constraintName.toUpperCase().equals("NOT_NULL")){
                        bool = true;
                    } else {
                        System.out.println("ERROR: INVALID Constraint. Please Add PRIMARY KEY and FOREIGN KEY in end arguments.");
                    }
                } else if(arrayCol.length==2) {
                    bool = true;
                } else if (arrayCol.length > 3){
                    System.out.println("ERROR: Too Many Constraints Provided.");
                } else {
                    System.out.println("ERROR: Incorrect Syntax in Table Definition.");
                }
            } else {
                System.out.println("ERROR: Invalid Data type. Supported datatypes are (INT,VARCHAR,FLOAT)");
            }
        }
        
        if(columnName.toUpperCase().equals("PRIMARY_KEY")){
            int tableNameStartIndex = coumnDef.indexOf("("); //+10
            int tableNameEndIndex = coumnDef.length();
            String column = coumnDef.substring(tableNameStartIndex+1,tableNameEndIndex-1).trim();
            
            if(columnList.contains(column)){
                if(coumnDef.matches(pkPattern)) {
                    bool = true;
                } else {
                    bool = false;
                    System.out.println("ERROR: Invalid PRIMARY KEY Syntax.");
                }
            } else {
                System.out.println("ERROR: Column Referenced In Primary Key Does Not Exists In Table Definition.");
            }
            
        }
        
        if(columnName.toUpperCase().equals("FOREIGN_KEY") || columnName.toUpperCase().equals("CONSTRAINT")) {
            int lengthOfReference = 10;
            int tableNameStartIndex = coumnDef.indexOf("REFERENCES")+lengthOfReference; //+10
            int tableNameEndIndex = coumnDef.indexOf("(",tableNameStartIndex);
            String table = coumnDef.substring(tableNameStartIndex,tableNameEndIndex).trim();
            String column = coumnDef.substring(tableNameEndIndex+1,coumnDef.length()-1).trim();
            
            String[] columnList = new String[1];
            columnList[0] = column;
            
            // Check if referenced table exists in the database.
            if(tableValidationMethods.checkTableAndColumn(table,columnList,databaseName)){
                if (coumnDef.matches(fkPattern)) {
                    bool = true;
                } else if(coumnDef.matches(fkPattern2)){
                    bool = true;
                } else {
                    bool = false;
                    System.out.println("ERROR: Invalid FOREIGN KEY Syntax.");
                }
            } else {
                System.out.println("ERROR: Referenced Column In FOREIGN KEY Does Not Exists In Database.");
            }
            
        }
        return bool;
    }
}

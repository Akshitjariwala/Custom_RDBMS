package queryValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class updateValidation {
    
    public static tableValidationMethods tableValidationMethods = new tableValidationMethods();
    
    public static Map<String,Object> validateUpdate(String[] queryTokens, String query,String databaseName) throws IOException {
        boolean isValid=false;
        String updatePattern = "[U-u][P-p][D-d][A-a][T-t][E-e]\\s+[A-Za-z0-9_]+\\s+[S-s][E-e][T-t]";
        int indexOfWhere = query.toUpperCase().indexOf("WHERE");
        int indexOfSet = query.toUpperCase().indexOf("SET")+3;
        int whereEndIndex = indexOfWhere + 5;
        String whereSubString = "";
        String setEndSubString = query.substring(0,indexOfSet);
        Map<String,Object> tokens = new HashMap<>();
        String tableName = queryTokens[1];
        tokens.put("tableName",tableName);
        boolean setClause = false;
        
        if(setEndSubString.matches(updatePattern)){
            if(tableValidationMethods.containsWhere(queryTokens)){
                whereSubString = query.substring(whereEndIndex,query.length()).trim().replaceAll("\\s*","");
                String[] wherelist = whereSubString.replaceAll("\"","").split("\\s*(?i)and\\s*");
                tokens.put("whereList",tableValidationMethods.createListFromArray(wherelist));
                for(int k=0;k<wherelist.length;k++){
                    if(tableValidationMethods.validateWhereClause(wherelist[k])){
                        if(!(wherelist[k].equals("")) && (Character.compare(whereSubString.charAt(whereSubString.length()-1),',') != 0)){
                            String[] whereColumnList = wherelist[k].replaceAll("\"","").split("=");
                            tokens.put("whereColumnList",tableValidationMethods.createListFromArray(whereColumnList));
                            for(int m=0;m<whereColumnList.length;m=m+2){
                                String[] list = {whereColumnList[0]};
                                if(tableValidationMethods.checkTableAndColumn(tableName,list,databaseName)){ // Check semantics for table and columnlist. Pass #tableName and column list.
                                    isValid = true;
                                    setClause = true;
                                } else {
                                    isValid = false;
                                    setClause = false;
                                    System.out.println("ERROR: Invalid Table Or Column Names.");
                                    break;
                                }
                            }
                        } else {
                            isValid = false;
                            setClause = false;
                            System.out.println("ERROR: Incorrect Syntax In Where clause.");
                            break;
                        }
                    } else {
                        isValid = false;
                        setClause = false;
                        System.out.println("ERROR: Incorrect Syntax In Where Clause.");
                        break;
                    }
                }
            } else {
                indexOfWhere = query.length();
                String noWhereSubString = query.substring(indexOfSet+3,query.length()).trim().replaceAll("\\s*","");
                String[] noWhereSubStringArray = noWhereSubString.split(",");
                tokens.put("setClause",tableValidationMethods.createListFromArray(noWhereSubStringArray));
                for(int i =0;i<noWhereSubStringArray.length;i++){
                    if(!(noWhereSubStringArray[i].equals("")) && (Character.compare(noWhereSubString.charAt(noWhereSubString.length()-1),',') != 0)){
                        setClause = true;
                    } else {
                        setClause = false;
                    }
                }
            }
        } else {
            System.out.println("ERROR: Incorrect SQL Syntax. Please Enter Valid Query.");
        }
        
        
        if(setClause){
            String setString = query.substring(indexOfSet,indexOfWhere).trim().replaceAll("\\s*","");
            String[] setArray = setString.replaceAll("\"","").split("\\s*,\\s*");
            tokens.put("setColumns",tableValidationMethods.createListFromArray(setArray));
            for(int j=0;j<setArray.length;j++){
                if(tableValidationMethods.validateWhereClause(setArray[j])){
                    if(!(setArray[j].equals("")) && (Character.compare(setString.charAt(setString.length()-1),',') != 0)){
                        String[] columnList = setArray[j].replaceAll("\"","").split("=");
                        tokens.put("setColumnList",tableValidationMethods.createListFromArray(columnList));
                        for(int m=0;m<columnList.length;m=m+2){
                            String[] list = {columnList[0]};
                            if(tableValidationMethods.checkTableAndColumn(tableName,list,databaseName)){ // Check semantics for table and columnlist. Pass #tableName and column list.
                                setClause = true;
                                isValid = true;
                            } else {
                                System.out.println("ERROR: Invalid Column Names.");
                                isValid = false;
                                break;
                            }
                        }
                    } else {
                        setClause = false;
                        isValid = false;
                        System.out.println("ERROR: Incorrect Syntax In Set Clause.");
                        break;
                    }
                } else {
                    isValid = false;
                    setClause = false;
                    System.out.println("ERROR: Incorrect Syntax In Set Clause.");
                    break;
                }
            }
        } else {
            System.out.println("ERROR: Incorrect SQL Syntax. PLease Enter Query Again.");
        }
        // Check WHERE clause
        if(isValid){
            tokens.put("isValid",true);
            System.out.println("SUCCESS: Entered UPDATE Query Is Valid.");
        } else {
            tokens.put("isValid",false);
        }
        
        //System.out.println(tokens);
        return tokens;
    }
}

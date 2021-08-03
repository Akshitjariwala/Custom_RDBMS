package queryValidator;

import dataDictionary.DataDictionary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class tableValidationMethods {
    
    public static DataDictionary dataDictionary = new DataDictionary();
    public static String currentDirectory = System.getProperty("user.dir");
    
    public static boolean validateWhereClause(String subQuery){
        String regExPattern = "[a-zA-Z0-9_]+\\s*=\\s*[a-zA-Z0-9_@.,\"\\s*]+";
        boolean result = false;
        if(Pattern.matches(regExPattern,subQuery)){
            result = true;
        }
        return result;
    }
    
    public static boolean containsWhere(String[] queryToken){
        boolean bool = false;
        
        for(int i = 0;i<queryToken.length;i++){
            queryToken[i] = queryToken[i].toUpperCase();
        }
        
        if(Arrays.asList(queryToken).contains("WHERE")){
            bool = true;
        }
        return bool;
    }
    
    public static boolean databaseExists(String databaseName){
        boolean ifExists = false;
        String databasePath = currentDirectory+"/appdata/database/"+databaseName;
        File database = new File(databasePath);
        
        if(database.exists()) {
            ifExists = true;
        }
        
        return ifExists;
    }
    
    public static boolean checkTableAndColumn(String tableName,String[] columnList,String databaseName) throws IOException {
        boolean valid = false;
        Map<String, List<String>> tableDictionary = dataDictionary.getDataDictionary(databaseName);
        List<String> attributeList = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        String subString="";
        String columnName = "";
        
        if(tableDictionary.containsKey(tableName)) {
            attributeList = tableDictionary.get(tableName);
            
            for(int k=0;k<attributeList.size();k++){
                String[] columnNameListTemp = attributeList.get(k).split("\\s+");
                for(int i=0;i<columnNameListTemp.length;i++) {
                    columnName = columnNameListTemp[0];
                    columnNameList.add(columnName);
                    break;
                }
            }
            
            for(int j=0;j<columnList.length;j++){
                if(columnNameList.contains(columnList[j])){
                    valid = true;
                    continue;
                } else {
                    valid = false;
                    break;
                }
            }
        } else {
            System.out.println("ERROR: Table Does Not Exists In The Database.");
            valid = false;
        }
        return valid;
    }
 
    public static boolean checkTable(String tableName,String databaseName) throws IOException {
        boolean valid = false;
        Map<String,List<String>> tableDictionary = dataDictionary.getDataDictionary(databaseName);
        
        if(tableDictionary.containsKey(tableName)) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }
    
    public static List<String> createListFromArray(String[] strArray){
        
        List<String> tokenList = Arrays.asList(strArray);
        
        return tokenList;
    }
}

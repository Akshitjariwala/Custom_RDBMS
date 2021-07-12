package queryValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {

    // Query Logs and check input for numbers and string.

    private static BufferedReader inputReader = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        QueryValidator();
    }

    public static boolean QueryValidator() throws IOException {
        String regex = "";
        String SQL = "";
        boolean found = false;
        boolean valid = false;
        Pattern pattern = Pattern.compile(regex);
        do{
            System.out.print("Enter query : ");
            SQL = inputReader.readLine();
            valid = validate(SQL);
        }while(!valid);
        Matcher matcher = pattern.matcher(SQL);
        while (matcher.find()) {
            found = true;
        }
        return found;
    }

    public static boolean validate(String sqlString) {
        String[] queryLanguageTokens = {"SELECT","INSERT","DELETE","UPDATE","ALTER","DROP","CREATE"};
        boolean queryIsValid = false;
        String queryToken = null;
        String[] queryTokens = sqlString.split(" ");

            // Validate valid query type.
            if(Arrays.asList(queryLanguageTokens).contains(queryTokens[0].toUpperCase())) {
                queryToken = queryTokens[0].toUpperCase();

                switch(queryToken){
                    case "SELECT" : queryIsValid = validateSelect(queryTokens,sqlString);break;
                    case "INSERT" : queryIsValid = validateInsert(queryTokens);break;
                    case "DELETE" : queryIsValid = validateDelete(queryTokens);break;
                    case "UPDATE" : queryIsValid = validateUpdate(queryTokens);break;
                    case "ALTER"  : queryIsValid = validateAlter(queryTokens);break;
                    case "DROP"   : queryIsValid = validateDrop(queryTokens);break;
                    case "CREATE" : queryIsValid = validateCreate(queryTokens);break;
                }
            }
            else {
                System.out.println("Error in SQL syntax. Please enter query again.");
            }
        return queryIsValid;
    }

    public static boolean validateSelect(String[] queryTokens, String query){
        boolean isValid=false;
        boolean mustClauseFlag = false;
        boolean tableColumnFlag = false;
        ArrayList<String> whereClauseArray = new ArrayList<String>();
        String optionalClause = "WHERE";
        String mustClause = "FROM";
        String tableName = "";
        int index=0;
        boolean whereFlag = false;

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
                if(!Arrays.asList(optionalClause).contains(queryTokens[index+1].toUpperCase())) {
                    tableName = queryTokens[index+1];
                    if(true) {   // Perform semantic analysis on Table Name. Check if table exists in the system.
                        if(queryTokens[1].equals("*")){
                            tableColumnFlag = true;
                        } else {
                            int indexOfFrom = query.toUpperCase().indexOf("FROM");
                            String colSubString = query.substring(6,indexOfFrom);
                            String[] colString = colSubString.split(",");

                            for(int i=0;i<colString.length;i++){
                                colString[i] = colString[i].trim();
                                if(true){ //semantic analysis for columns
                                    tableColumnFlag = true;
                                } else {
                                    System.out.println("No such table named "+tableName+" present in the database.");
                                    tableColumnFlag = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("No such table named "+tableName+" present in the database.");
                    }
                } else {
                    System.out.println("No Table name provided.Enter Query Again.");
                }
            } else {
                System.out.println("No Table name provided.Enter Query Again.");
            }
        } else {
            System.out.println("Invalid Query Syntax. Required clauses not provided. Please Enter Query Again.");
        }

        if(tableColumnFlag) {
            if (queryTokens[index + 2].toUpperCase().equals("WHERE")) {
                // Convert into subarray of where.
                for (int i=index + 3;i<queryTokens.length;i++){
                    whereClauseArray.add(queryTokens[i]);
                }

                int indexOfWhere = query.toUpperCase().indexOf("WHERE");
                int indexOfNextWord = indexOfWhere + 5;
                String subString = query.substring(indexOfNextWord,query.length());

                if(whereClauseArray.contains("AND") || whereClauseArray.contains("and")) {

                    String[] subArray = subString.split("\\s[A-a][N-n][D-d]\\s");

                    for(int j=0;j<subArray.length;j++){
                        subArray[j] = subArray[j].trim();
                        if(validateWhereClause(subArray[j])){
                            isValid = true;
                        } else {
                            isValid = false;
                        }
                    }
                } else {
                    if(validateWhereClause(subString)){
                        isValid = true;
                    }
                }
            } else {
                isValid = true;
            }
        }
        System.out.println("Entered SELECT query is VALID.");
        return isValid;
    }

    public static boolean validateWhereClause(String subquery){
        String regExPattern = "[a-zA-Z0-9]*\\s+=\\s+[a-zA-Z0-9]*";
        boolean result = false;
        if(Pattern.matches(regExPattern,subquery)){
            result = true;
        }
        return result;
    }

    public static boolean validateInsert(String[] queryTokens){
        boolean isValid=false;
        String mustClause = "FROM";
        // Validate must clauses.
        for(int i = 0; i < queryTokens.length; i++){
            if(mustClause == queryTokens[i].toUpperCase()){
                System.out.println("Valid query");
                isValid = true;
                break;
            } else {
                System.out.println("Error in SQL syntax. Please enter query again.");
            }
        }


        return isValid;
    }

    public static boolean validateDelete(String[] queryTokens){
        boolean isValid=false;

        return isValid;
    }

    public static boolean validateUpdate(String[] queryTokens){
        boolean isValid=false;

        return isValid;
    }

    public static boolean validateAlter(String[] queryTokens){
        boolean isValid=false;

        return isValid;
    }

    public static boolean validateDrop(String[] queryTokens){
        boolean isValid=false;

        return isValid;
    }

    public static boolean validateCreate(String[] queryTokens){
        boolean isValid=false;

        return isValid;
    }
}



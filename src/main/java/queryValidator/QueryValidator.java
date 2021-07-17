package queryValidator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {

    // Query Logs and check input for numbers and string. Check for single query and transaction. Check for Semantic analysis.

    public static String[] tyepArray = {"INT","FLOAT","VARCHAR"};
    public static String tableNamePattern = "[A-Za-z0-9_]+";
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();
    public static String databaseName = "database1";
    public static ArrayList<String> columnList = new ArrayList<>();

    private static BufferedReader inputReader = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        QueryValidator();
/*
        */
/*getDataDictionary();*//*

        checkTableAndColumn("table_a",columnList);
*/

    }

    public static boolean QueryValidator() throws IOException {
        String regex = "";
        String SQL = "";
        String tempSQL="";
        boolean found = false;
        boolean valid = false;
        boolean dbExists = false;
        boolean dbNotExists = false;
        boolean validInput = false;
        boolean mainMenu = false;
        boolean repeat = false;
        char input;
        Scanner userChoice = new Scanner( System.in );
        Scanner userInput = new Scanner( System.in );
        Pattern pattern = Pattern.compile(regex);
        do{
            System.out.println("\n\n----------------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\tDatabase Server\t\t\t\t\t");
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("1. Create Database.");
            System.out.println("2. Use Database.");
            System.out.println("\n Select 1 to Create Database and 2 to Use Database.\n");
            do {
                System.out.print("Enter Selection : ");
                int choice = userChoice.nextInt();
                if(choice >=1 && choice <=2){
                    validInput = true;
                    switch (choice) {
                        case 1:
                            do {
                                System.out.print("=> Enter query : ");
                                SQL = inputReader.readLine();
                                tempSQL = SQL.trim().replaceAll("\\s+", " ");
                                String[] queryTokens = tempSQL.split(" ");
                                valid = validateCreateDatabase(tempSQL);
                                if (valid) {
                                    databaseName = queryTokens[2];
                                    if (!databaseExists(databaseName)) {
                                        dbNotExists = true;
                                        // *********** pass valid to execution.**********************************///
                                        mainMenu = true;
                                    } else {
                                        dbNotExists = false;
                                        System.out.println("ERROR: DATABASE Already Exists In The System. Choose a Different Database Name");
                                    }
                                }
                            } while (!dbNotExists);
                            break;
                        case 2:
                            do {
                                System.out.print("Enter Database Name : ");
                                databaseName = inputReader.readLine();
                                if (databaseExists(databaseName)) {
                                    dbExists = true;
                                    System.out.println("SUCCESS: CONNECTED TO DATABASE");
                                } else {
                                    dbExists = false;
                                    System.out.println("ERROR: DATABASE Does Not Exist In The System. Enter Valid Database Name");
                                }
                            } while (!dbExists);
                            do {
                                do {
                                    System.out.print("=> Enter query : ");
                                    SQL = inputReader.readLine();
                                    tempSQL = SQL.trim().replaceAll("\\s+", " ");
                                    String[] queryTokens = tempSQL.split(" ");
                                    if (queryTokens[0].toUpperCase().equals("CREATE") && queryTokens.length >= 2) {
                                        if (queryTokens[1].toUpperCase().equals("DATABASE")) {
                                            valid = false;
                                            System.out.println("ERROR: Cannot Create Database Inside A Database.");
                                        } else {
                                            valid = validate(SQL);
                                            // if valid pass to execution module.
                                            if (valid) {
                                                //*********** if valid true. pass to execution module.**********************************//
                                                System.out.print("Do you want to continue (Y/N) : ");
                                                input = userInput.next().charAt(0);
                                                if(Character.compare(input,'Y') == 0 || Character.compare(input,'y') == 0) {
                                                    repeat = true;
                                                    mainMenu = true;
                                                } else if(Character.compare(input,'N') == 0 || Character.compare(input,'n') == 0){
                                                    repeat = false;
                                                } else {
                                                    System.out.println("ERROR: Invalid Input.");
                                                }
                                            }
                                        }
                                    } else {
                                        valid = validate(SQL);

                                        if (valid) {
                                            //*********** if valid true. pass to execution module.**********************************//
                                            System.out.print("Do you want to continue (Y/N) : ");
                                            input = userInput.next().charAt(0);
                                            if(Character.compare(input,'Y') == 0 || Character.compare(input,'y') == 0) {
                                                repeat = true;
                                                mainMenu = true;
                                            } else if(Character.compare(input,'N') == 0 || Character.compare(input,'n') == 0){
                                                repeat = false;
                                            } else {
                                                System.out.println("ERROR: Invalid Input.");
                                            }
                                        }
                                    }
                                } while (!valid);
                            }while(repeat);
                            break;
                        }
                } else {
                    validInput = false;
                    System.out.println("ERROR: Please Enter Valid Input.");
                }

            }while(!validInput);
        }while(mainMenu);
        Matcher matcher = pattern.matcher(SQL);
        while (matcher.find()) {
            found = true;
        }
        return found;
    }

    public static boolean validateCreateDatabase(String sqlString) {
        String queryToken = null;
        int queryTypeIndex = 1;
        sqlString = sqlString.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlString.split(" ");
        boolean isValid = false;

        if (queryTokens.length == 3) {
            if (true) { // Check if database exists.
                isValid = true;
            } else {
                System.out.println("ERROR: Database Already Exists.");
            }
        } else {
            System.out.println("ERROR: Invalid CREATE Statement.");
        }

        if(isValid) {
            System.out.println("SUCCESS: Entered CREATE Query Is Valid.");
        }

        return isValid;
    }

    public static boolean validate(String sqlString) throws IOException {
        String[] queryLanguageTokens = {"SELECT","INSERT","DELETE","UPDATE","ALTER","DROP","CREATE"};
        boolean queryIsValid = false;
        String queryToken = null;
        sqlString = sqlString.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlString.split(" ");

            // Validate valid query type.
            if(Arrays.asList(queryLanguageTokens).contains(queryTokens[0].toUpperCase())) {
                queryToken = queryTokens[0].toUpperCase();

                switch(queryToken){
                    case "SELECT" : queryIsValid = validateSelect(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "INSERT" : queryIsValid = validateInsert(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "DELETE" : queryIsValid = validateDelete(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "UPDATE" : queryIsValid = validateUpdate(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "ALTER"  : queryIsValid = validateAlter(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "DROP"   : queryIsValid = validateDrop(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "CREATE" : queryIsValid = validateCreate(queryTokens,sqlString);
                                    if(queryIsValid) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                }
            }
            else {
                System.out.println("ERROR: Error in SQL syntax. Please enter query again.");
            }
        return queryIsValid;
    }

    public static boolean validateSelect(String[] queryTokens, String query) throws IOException {
        boolean isValid=false;
        boolean mustClauseFlag = false;
        boolean tableColumnFlag = false;
        ArrayList<String> whereClauseArray = new ArrayList<String>();
        String optionalClause = "WHERE";
        String mustClause = "FROM";
        String tableName = "";
        int index=0;

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
                    if(checkTable(tableName)) {   // Perform semantic analysis on Table Name. Check if table exists in the system.
                        if(queryTokens[1].equals("*")){
                            tableColumnFlag = true;
                        } else {
                            int indexOfFrom = query.toUpperCase().indexOf("FROM");
                            String colSubString = query.substring(6,indexOfFrom);
                            colSubString = colSubString.replaceAll("\\s*","");
                            if(colSubString.charAt(colSubString.length() - 1) != ','){
                                String[] colString = colSubString.split("\\s*,\\s*");
                                for(int i=0;i<colString.length;i++){
                                    if(!(colString[i].equals("")) && colString[i].matches("[A-Za-z0-9]+")){
                                        if(checkTableAndColumn(tableName,colString)){ //semantic analysis for columns
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
            if((index + 1) != queryTokens.length){
                if (queryTokens[index + 2].toUpperCase().equals("WHERE")) {
                    // Convert into subarray of where.
                    for (int i=index + 3;i<queryTokens.length;i++){
                        whereClauseArray.add(queryTokens[i]);
                    }

                    int indexOfWhere = query.toUpperCase().indexOf("WHERE");
                    int indexOfNextWord = indexOfWhere + 5;
                    String subString = query.substring(indexOfNextWord+1,query.length());

                    if(whereClauseArray.contains("AND") || whereClauseArray.contains("and")) {

                        String[] subArray = subString.split("\\s+[A-a][N-n][D-d]\\s+");

                        for(int j=0;j<subArray.length;j++){
                            subArray[j] = subArray[j].trim();
                            if(validateWhereClause(subArray[j])){
                                isValid = true;
                            } else {
                                System.out.println("ERROR: Invalid SQL Syntax. Error in WHERE clause.");
                                isValid = false;
                                break;
                            }
                        }
                    } else {
                        if(validateWhereClause(subString.trim())){
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
        if(isValid){
            System.out.println("SUCCESS: Entered SELECT query is VALID.");
        } else {
            System.out.println("");
        }

        return isValid;
    }

    public static boolean validateWhereClause(String subQuery){
        String regExPattern = "[a-zA-Z0-9_]+\\s*=\\s*[a-zA-Z0-9_]+";
        boolean result = false;
        if(Pattern.matches(regExPattern,subQuery)){
            result = true;
        }
        return result;
    }

    public static boolean validateInsert(String[] queryTokens, String query) throws IOException {
        boolean isValid=false;
        int indexOfColumns = query.toUpperCase().indexOf("(");
        int indexOfColumnsEnd = query.toUpperCase().indexOf(")");
        int lengthOfValues = 7;
        String columnSubString = "";
        String valuesSubString = "";
        int indexOfValues = query.toUpperCase().indexOf("VALUES");
        int indexOfValuesEnd = query.toUpperCase().indexOf(")",indexOfValues);
        String[] tempArray = new String[queryTokens.length];
        String insertPattern = "[I-i][N-n][S-s][E-e][R-r][T-t]\\s+[I-i][N-n][T-t][O-o]\\s+[A-Za-z0-9_]+\\([A-Za-z0-9,_]+\\)\\s*[V-v][A-a][L-l][U-u][E-e][S-s]\\s*\\([A-Za-z0-9,_]+\\)";
        int indexOfInto = query.toUpperCase().indexOf("INTO");
        String tableName = query.substring(indexOfInto+4,query.indexOf("(")).trim();
        // To upper case
        for(int j=0;j<queryTokens.length;j++){
            tempArray[j] =  queryTokens[j].toUpperCase();
        }

        if(query.matches(insertPattern)){
            columnSubString = query.substring(indexOfColumns+1,indexOfColumnsEnd);
            columnSubString = columnSubString.trim();
            String[] columnsArray = columnSubString.split("\\s*,\\s*");
            valuesSubString = query.substring(indexOfValues+lengthOfValues,indexOfValuesEnd);
            valuesSubString = valuesSubString.trim();
            String[] valuesArray = valuesSubString.split("\\s*,\\s*");
            if(columnsArray.length == valuesArray.length){
                for(int i=0;i<columnsArray.length;i++){
                    if(!(columnsArray[i].equals("")) && columnsArray[i].matches("[A-Za-z0-9]+") && !(valuesArray[i].equals("")) && valuesArray[i].matches("[A-Za-z0-9]+")){
                        if(checkTableAndColumn(tableName,columnsArray)) { // perform semantic analysis to check if columns exits and belongs to table. Pass #tableName and columnArray.
                            isValid = true;
                        } else {
                            isValid = false;
                            System.out.println("ERROR: Error in SQL syntax. Columns does not match with Table "+tableName);
                            break;
                        }
                    } else {
                        isValid = false;
                        System.out.println("ERROR: Error in SQL syntax. Columns in query does not match.");
                        break;
                    }
                }
            } else {
                System.out.println("ERROR: Error in SQL syntax. Columns in query does not match.");
            }
        }
        else {
           System.out.println("ERROR: Invalid query");
        }

        if(isValid){
            System.out.println("SUCCESS: Entered INSERT query is valid.");
        }

        return isValid;
    }

    public static boolean validateDelete(String[] queryTokens,String query) throws IOException {
        boolean isValid=false;
        String deletePattern = "[D-d][E-e][L-l][E-e][T-t][E-e]\\s+[F-f][R-r][O-o][M-m]\\s+[A-Za-z0-9_]+\\s*";
        String whereSubString = "";
        String deleteSubString = "";
        int whereLength = 5;
        String tableName = queryTokens[2];
        int indexWhere = query.toUpperCase().indexOf("WHERE");

        // check if contains where clause and table name.
        if(containsWhere(queryTokens)) {
            if(!queryTokens[2].toUpperCase().equals("WHERE")){
                deleteSubString = query.substring(0,indexWhere);
                if(deleteSubString.matches(deletePattern)) {
                    whereSubString = query.substring(indexWhere+whereLength+1);
                    String[] whereArray = whereSubString.split("\\s+[A-a][N-n][D-d]\\s+");
                    for(int i=0;i<whereArray.length;i++){
                        if(validateWhereClause(whereArray[i])){
                            if(checkTable(tableName)){ // Perform semantic analysis on tableName.
                                // Convert where clauses into array of columns
                                String[] columnsArray = whereArray[i].split("\\s*=\\s*");
                                for(int j=0;j<columnsArray.length;j++){
                                    String[] list = {columnsArray[0]};
                                    if(checkTableAndColumn(tableName,list)){ // Perform semantic analysis on columns. Pass #columnsArray.
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
                            System.out.println("ERROR: Error in Where Clause");
                            isValid = false;
                            break;
                        }
                    }
                } else {
                    System.out.println("ERROR: Invalid query");
                }
            } else {
                System.out.println("ERROR: No Table name provided. Please provide Table name in the query.");
                isValid = false;
            }

        } else {
            if(queryTokens.length == 3){
                isValid = true;
            } else {
                isValid = false;
                System.out.println("ERROR: Incorrect syntax in Where clause.");
            }
        }

        if(isValid){
            System.out.println("SUCCESS: Entered DELETE query is valid.");
        }

        return isValid;
    }

    public static boolean validateUpdate(String[] queryTokens,String query) throws IOException {
        boolean isValid=false;
        String updatePattern = "[U-u][P-p][D-d][A-a][T-t][E-e]\\s+[A-Za-z0-9_]+\\s+[S-s][E-e][T-t]";
        int indexOfWhere = query.toUpperCase().indexOf("WHERE");
        int indexOfSet = query.toUpperCase().indexOf("SET")+3;
        int whereEndIndex = indexOfWhere + 5;
        String whereSubString = "";
        String setEndSubString = query.substring(0,indexOfSet);
        String tableName = queryTokens[1];
        boolean setClause = false;

        if(setEndSubString.matches(updatePattern)){
            if(containsWhere(queryTokens)){
                whereSubString = query.substring(whereEndIndex,query.length()).trim().replaceAll("\\s*","");
                String[] wherelist = whereSubString.split("\\s*[A-a][N-n][D-d]\\s*");
                for(int k=0;k<wherelist.length;k++){
                    if(validateWhereClause(wherelist[k])){
                        if(!(wherelist[k].equals("")) && (Character.compare(whereSubString.charAt(whereSubString.length()-1),',') != 0)){
                            String[] whereColumnList = wherelist[k].split("=");
                            for(int m=0;m<whereColumnList.length;m=m+2){
                                String[] list = {whereColumnList[0]};
                                if(checkTableAndColumn(tableName,list)){ // Check semantics for table and columnlist. Pass #tableName and column list.
                                    isValid = true;
                                    setClause = true;
                                } else {
                                    isValid = false;
                                    setClause = false;
                                    System.out.println("ERROR: Invalid Table or Column Names.");
                                    break;
                                }
                            }
                        } else {
                            isValid = false;
                            setClause = false;
                            System.out.println("ERROR: Incorrect syntax in Where clause. 3");
                            break;
                        }
                    } else {
                        isValid = false;
                        setClause = false;
                        System.out.println("ERROR: Incorrect syntax in Set clause. 4");
                        break;
                    }
                }
            } else {
                indexOfWhere = query.length();
                String noWhereSubString = query.substring(indexOfSet+3,query.length()).trim().replaceAll("\\s*","");
                String[] noWhereSubStringArray = noWhereSubString.split(",");
                for(int i =0;i<noWhereSubStringArray.length;i++){
                    if(!(noWhereSubStringArray[i].equals("")) && (Character.compare(noWhereSubString.charAt(noWhereSubString.length()-1),',') != 0)){
                        setClause = true;
                    } else {
                        setClause = false;
                    }
                }
            }
        } else {
            System.out.println("ERROR: Incorrect SQL syntax. Please enter Valid query.");
        }


        if(setClause){
            String setString = query.substring(indexOfSet,indexOfWhere).trim().replaceAll("\\s*","");
            String[] setArray = setString.split("\\s*,\\s*");
            for(int j=0;j<setArray.length;j++){
                if(validateWhereClause(setArray[j])){
                    if(!(setArray[j].equals("")) && (Character.compare(setString.charAt(setString.length()-1),',') != 0)){
                        String[] columnList = setArray[j].split("=");
                        for(int m=0;m<columnList.length;m=m+2){
                            if(checkTableAndColumn(tableName,columnList)){ // Check semantics for table and columnlist. Pass #tableName and column list.
                                setClause = true;
                                isValid = true;
                            } else {
                                System.out.println("ERROR: Invalid Column Names.");
                                break;
                            }
                        }
                    } else {
                        setClause = false;
                        isValid = false;
                        System.out.println("ERROR: Incorrect syntax in Set clause.");
                        break;
                    }
                } else {
                    isValid = false;
                    setClause = false;
                    System.out.println("ERROR: Incorrect syntax in Set clause.");
                    break;
                }
            }
        } else {
            System.out.println("ERROR: Incorrect SQL syntax. PLease enter query again.");
        }
        // Check WHERE clause
        if(isValid){
            System.out.println("SUCCESS: Entered UPDATE query is valid.");
        }

        return isValid;
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

    public static boolean validateAlter(String[] queryTokens,String query) throws IOException {
        boolean isValid=false;
        String alterPattern = "[A-a][L-l][T-t][E-e][R-r]\\s+[T-t][A-a][B-b][L-l][E-e]\\s+[A-Za-z0-9_]+";
        String[] alterClauses = {"ADD","MODIFY","DROP","CHANGE","RENAME_TO"};
        String alterSyntax = "[A-Za-z0-9_]+\\s+[A-Za-z0-9]+";
        String alterClause="";
        String alterQuery = "";
        String tableName =  queryTokens[2];
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
                        String newTableName = queryTokens[querylength];
                        if(newTableName.matches("[A-Za-z0-9_]+")){
                            table = true;
                        } else {
                            System.out.println("ERROR: Invalid table name. Table Name can only contain alpha numeric values and '_' ");
                        }
                    } else {
                        table = true;
                        columnName = queryTokens[querylength];
                    }
                } else {
                    System.out.println("ERROR: Incorrect SQL syntax in "+alterClause+" .Please enter query again.");
                }
            }

            if(alterClause.equals("ADD") || alterClause.equals("MODIFY")) {
                if(alterQuery.matches(alterSyntax)){
                    switch(alterClause) {
                        case "ADD"    : columnName = queryTokens[querylength]; columnDatatype = queryTokens[querylength+1];
                                        if(Arrays.asList(tyepArray).contains(columnDatatype.toUpperCase()))
                                        { table = true; } else {
                                            System.out.println("ERROR: Invalid datatype.");
                                        } break;
                        case "MODIFY" : columnName = queryTokens[querylength]; columnDatatype = queryTokens[querylength+1];
                                        if(Arrays.asList(tyepArray).contains(columnDatatype.toUpperCase()))
                                        { table = true; } else {
                                            System.out.println("ERROR: Invalid Data Type. Supported datatypes are (INT,VARCHAR,FLOAT)");
                                        } break;
                    }
                } else {
                    System.out.println("ERROR: InCorrect syntax in "+alterClause+" ");
                }
            }

            if(alterClause.equals("CHANGE")) {
                    if(querylength+3 == queryTokens.length) {
                        columnName = queryTokens[querylength]; newColumnName = queryTokens[querylength+1]; columnDatatype = queryTokens[querylength+2];
                        if (Arrays.asList(tyepArray).contains(columnDatatype.toUpperCase())) {
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
        columnList[0] = columnName;

        // Perform semantic analysis. Pass #TableName.
        if(table) {
            if(checkTable(tableName)){
                if(!(alterClause.toUpperCase().equals("RENAME_TO"))){
                    if(checkTableAndColumn(tableName,columnList)) {
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
            System.out.println("SUCCESS: Entered ALTER query is valid.");
        }

        return isValid;
    }

    public static boolean validateDrop(String[] queryTokens,String query) throws IOException {
        boolean isValid=false;
        String dropPattern = "[D-d][R-r][O-o][P-p]\\s+[T-t][A-a][B-b][L-l][E-e]\\s+[A-Za-z0-9_]+";


        if(query.matches(dropPattern)) {
            String tableName = queryTokens[2];
            if(checkTable(tableName)) { //Perform semantic analysis. Pass #TableName.
                isValid = true;
            } else {
                System.out.println("ERROR: Table Does Not Exists In The Database.");
                isValid = false;
            }
        } else {
            System.out.println("ERROR: Incorrect DROP syntax.");
        }

        if(isValid) {
            System.out.println("SUCCESS: Entered DROP query is valid.");
        }

        return isValid;
    }

    public static boolean validateCreate(String[] queryTokens,String query) throws IOException {
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

        if(queryTokens[queryTypeIndex].toUpperCase().equals("TABLE")) {
            tableName = queryTokens[2];
            if(!checkTable(tableName)) { //Perform semantic analysis. Pass #TableName.
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
                    for(int i=0;i<creationArray.length;i++){
                        if(!creationArray[i].matches("\\s*")) {
                            if(validateColumnDefinition(creationArray[i].trim())){
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
            System.out.println("SUCCESS: Entered CREATE query is valid.");
        }

        return isValid;
    }

    public static boolean validateColumnDefinition(String coumnDef) throws IOException {
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
            if(Arrays.asList(tyepArray).contains(columnConstraint.toUpperCase())) {
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
            if(checkTableAndColumn(table,columnList)){
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

    public static void generateQueryLog(String query) throws IOException {
        //formatter.format(date)
        String fileName = "QueryLogs.txt";
        boolean fileExists = false;
        File queryLogFile = new File("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName+"/"+fileName);
        fileExists = queryLogFile.exists();
        FileWriter fileWriter = new FileWriter(queryLogFile, true);

        if(fileExists) {
            fileWriter.write(query+"\t|\t"+formatter.format(date));
            fileWriter.write(System.lineSeparator());
        } else {
            fileWriter.write("QUERY\t|\tTIME_STAMP");
            fileWriter.write(System.lineSeparator());
            fileWriter.write(query+"\t|\t"+formatter.format(date));
            fileWriter.write(System.lineSeparator());
        }
        fileWriter.close();
    }

    public static boolean databaseExists(String databaseName){
        boolean ifExists = false;
        String databasePath = "D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName;
        File database = new File(databasePath);

        if(database.exists()) {
            ifExists = true;
        }

        return ifExists;
    }

    public static Map<String,List<String>> getDataDictionary() throws IOException {
        String dataDictionary = "data_dictionary.txt";
        File dataDictionaryFile = new File("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName+"/"+dataDictionary);
        Map<String,List<String>> tableDictionary = new HashMap<String,List<String>>();
        FileInputStream inputStream = new FileInputStream("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName+"/"+dataDictionary);
        BufferedReader bufferStream = new BufferedReader(new InputStreamReader(inputStream));
        String tableLine;
        String table_name=null;
        int separetorIndex;
        String dictionarySubString;

        if(dataDictionaryFile.exists()){
            while((tableLine = bufferStream.readLine()) != null){
                List<String> columnDefinitionList = new ArrayList<>();
                separetorIndex = tableLine.indexOf("||");
                table_name = tableLine.substring(0,separetorIndex).trim();
                if(!table_name.equals("TABLE NAME")){ // Removing first row.
                    dictionarySubString = tableLine.substring(separetorIndex+2,tableLine.length()).trim();
                    String[] columnDefinitionArray = dictionarySubString.split("\t|\t");
                    for(int i=0;i<columnDefinitionArray.length;i++){
                        if(!columnDefinitionArray[i].equals("|")){
                            columnDefinitionList.add(columnDefinitionArray[i]);
                        }
                    }
                    tableDictionary.put(table_name,columnDefinitionList);
                }
            }
        } else {
            System.out.println("Data Dictionary Does not exists.");
        }

        return tableDictionary;
    }

    public static boolean checkTableAndColumn(String tableName,String[] columnList) throws IOException {
        boolean valid = false;
        Map<String,List<String>> tableDictionary = getDataDictionary();
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

    public static boolean checkTable(String tableName) throws IOException {
        boolean valid = false;
        Map<String,List<String>> tableDictionary = getDataDictionary();

        if(tableDictionary.containsKey(tableName)) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }
}



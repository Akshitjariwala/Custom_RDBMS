package queryValidator;

import dataDictionary.DataDictionary;
import erdGenerator.ERDGenerator;
import queryProcessor.*;
import sqlDump.SqlDump;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {

    public static String currentDirectory = System.getProperty("user.dir");
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();
    public static String databaseName;
    public static ERDGenerator erdGenerator = new ERDGenerator();
    public static SqlDump sqlDump = new SqlDump();
    public static selectValidation validationSelect;
    public static insertValidation validationInsert;
    public static deleteValidation validationDelete;
    public static updateValidation validationUpdate;
    public static alterValidation validationAlter;
    public static dropValidation validationDrop;
    public static createValidation validationCreate;
    public static tableValidationMethods tableValidation = new tableValidationMethods();


    private static final BufferedReader inputReader = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        QueryValidator();
    }

    public static void QueryValidator() throws IOException {
        String regex = "";
        String SQL = "";
        String tempSQL;
        boolean found;
        boolean valid;
        boolean dbExists;
        boolean dbNotExists = false;
        boolean validInput;
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
            System.out.println("3. Create ERD.");
            System.out.println("4. Create SQL Dump.");
            System.out.println("5. Exit.");
            System.out.println("\n Select 1 to Create Database and 5 to Use Database.\n");
            do {
                System.out.print("Enter Selection : ");
                int choice = userChoice.nextInt();
                if(choice >=1 && choice <=5){
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
                                    if (!tableValidation.databaseExists(databaseName)) {
                                        dbNotExists = true;
                                        // *********** pass valid to execution.**********///
                                        CreateDB.execute(databaseName);
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
                                if (tableValidation.databaseExists(databaseName)) {
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
                        case 3 : createERD(); mainMenu = true; break;

                        case 4 : generateSQLDump();
                                mainMenu = true;
                                break;
                        case 5 : mainMenu = false; break;
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
    }

    public static boolean validate(String sqlString) throws IOException {
        String[] queryLanguageTokens = {"SELECT","INSERT","DELETE","UPDATE","ALTER","DROP","CREATE"};
        boolean queryIsValid = false;
        String queryToken = null;
        sqlString = sqlString.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlString.split(" ");
        Map<String,Object> validationTokens = new HashMap<>();

            // Validate valid query type.
            if(Arrays.asList(queryLanguageTokens).contains(queryTokens[0].toUpperCase())) {
                queryToken = queryTokens[0].toUpperCase();

                switch(queryToken){
                    case "SELECT" : validationSelect = new selectValidation();
                                    validationTokens = validationSelect.validateSelect(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                        System.out.println(validationTokens.toString());
                                        Select.setTokens(validationTokens);
                                    }
                                    // take validationTokens from here to get tokens.
                                    break;
                    case "INSERT" : validationInsert = new insertValidation();
                                    validationTokens = validationInsert.validateInsert(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                        Insert insert = new Insert();
                                        insert.insertData(validationTokens);
                                    }
                                    break;
                    case "DELETE" : validationDelete = new deleteValidation();
                                    validationTokens = validationDelete.validateDelete(queryTokens,sqlString,databaseName);
                                    //validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                        Delete.execute(validationTokens);
                                    }
                                    break;
                    case "UPDATE" : validationUpdate = new updateValidation();
                                    validationTokens = validationUpdate.validateUpdate(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                        Update.execute(validationTokens);
                                    }
                                    break;
                    case "ALTER"  : validationAlter = new  alterValidation();
                                    validationTokens = validationAlter.validateAlter(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "DROP"   : validationDrop = new dropValidation();
                                    validationTokens = validationDrop.validateDrop(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        generateQueryLog(sqlString);
                                    }
                                    break;
                    case "CREATE" : validationCreate = new createValidation();
                                    validationTokens = validationCreate.validateCreate(queryTokens,sqlString,databaseName);
                                    validationTokens.put("databaseName",databaseName);
                                    // validationTokens.put("sqlString",sqlString);
                                    if(validationTokens.get("isValid") == (Object)true) {
                                        sqlDump.storeCreateQuery(databaseName,sqlString,validationTokens.get("tableName").toString());
                                        generateQueryLog(sqlString);
                                        Create create = new Create();
                                        create.createDataDictionary(validationTokens);
                                        create.createTable(validationTokens);
                                    }
                                    break;
                }
            }
            else {
                System.out.println("ERROR: Error in SQL syntax. Please enter query again.");
            }
        return queryIsValid;
    }

    public static void generateQueryLog(String query) throws IOException {
        //formatter.format(date)
        String fileName = "QueryLogs.txt";
        boolean fileExists = false;
        File queryLogFile = new File(currentDirectory+"/appdata/database/"+databaseName+"/"+fileName);
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

    public static void createERD() throws IOException {
        String createERDPattern = "[C-c][R-r][E-e][A-a][T-t][E-e]\\s+[E-e][R-r][D-d]\\s+[A-Za-z0-9]+";
        Boolean flag = false;
        String databaseName;
        Boolean bool = false;
        do{
            System.out.print("Enter ERD Query : ");
            Scanner sc = new Scanner(System.in);
            String query = sc.nextLine();

            if(query.matches(createERDPattern)){
                String[] tokens = query.split("\\s+");
                databaseName = tokens[tokens.length-1];
                if(tableValidation.databaseExists(databaseName)){
                    flag = true;
                    bool = erdGenerator.createERD(databaseName);
                    if(bool){
                        System.out.println("SUCCESS: ERD File Generated.");
                        flag = true;
                    } else {
                        System.out.println("ERROR: ERD Generation Failed.");
                        flag = false;
                    }
                } else {
                    System.out.println("ERROR: Table does not exists in the database.");
                }
            } else {
                System.out.println("ERROR: Invalid Query Syntx. Please Enter Query Again.");
            }
        } while(flag == false);
    }
    
    public static void generateSQLDump(){
        Boolean flag = false;
        do{
            System.out.print("Enter Database Name : ");
            Scanner sc = new Scanner(System.in);
            String databaseName = sc.nextLine();
            
            if(tableValidation.databaseExists(databaseName)){
                sqlDump.getSQLDump(databaseName);
                System.out.println("SQL Dump for Database "+databaseName+" Generated Successfully.");
                flag = true;
            } else {
                System.out.println("ERROR: Database Does not exists in the system. Please Enter Valid Database Name.");
            }
            
        }while(!flag);
    }
    
    public static boolean validateCreateDatabase(String sqlString) {
        sqlString = sqlString.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlString.split(" ");
        boolean isValid = false;
        
        if (queryTokens.length == 3) {
            if (!tableValidation.databaseExists(queryTokens[2])) {
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
}



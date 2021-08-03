package Transaction;

import queryValidator.QueryValidator;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Transaction {
    
    public static String currentDirectory = System.getProperty("user.dir");
    public static String fileName = "transactionQueue.txt";
    public static TransactionHandler transactionHandler;
    public static QueryValidator queryValidator;
    
    public void startTransaction(String databaseName) {
        Boolean commitFlag = false;
        String SQL = "";
        Scanner inputReader = new Scanner( System.in );
        do{
            System.out.print("=> Enter query : ");
            SQL = inputReader.nextLine();
            if(SQL.toUpperCase().equals("COMMIT") || SQL.toUpperCase().equals("ROLLBACK")){
                if(SQL.toUpperCase().equals("ROLLBACK")) {
                    replaceFile(databaseName);
                } else {
                    executeTransaction(databaseName);
                }
                commitFlag = true;
            } else{
                addToTransactionQueue(SQL,databaseName);
            }
        } while(!commitFlag);
        
        // execute transaction.
    }
    
    public void addToTransactionQueue(String queryString,String databaseName) {
        FileWriter writer;
        String filePath = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
        try {
            writer = new FileWriter(filePath,true);
            writer.write(queryString);
            writer.write('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void replaceFile(String databaseName){
        String filePath = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
        String temp = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/temp.txt";
        
        File file = new File(filePath);
        File tempFile = new File(temp);
        try {
            Boolean bool = tempFile.createNewFile();
            System.out.println(bool);
            file.delete();
            File newName = new File(filePath);
            tempFile.renameTo(newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void executeTransaction(String databaseName){
        String filePath = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
        String query =  null;
        List<String> transactionQueryList = new ArrayList<>();
        queryValidator = new QueryValidator();
        transactionHandler = new TransactionHandler(databaseName);
        List<String> tableList = new ArrayList<>();
        
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while((query = reader.readLine()) != null) {
                transactionQueryList.add(query);
            }
            
            for(int i=0;i<transactionQueryList.size();i++) {
                String table = extractTable(transactionQueryList.get(i));
                String tablePath = databaseName+"/"+table;
                tableList.add(tablePath);
                if(!transactionHandler.checkLock(table)) {
                    transactionHandler.lockTable(tablePath);
                    queryValidator.validate(transactionQueryList.get(i));
                } else {
                    System.out.println("Table is Already Locked.");
                }
            }
            
            for(int i=0;i<tableList.size();i++){
                transactionHandler.unlockTable(tableList.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String extractTable(String queryString){
        String[] queryLanguageTokens = {"SELECT","INSERT","DELETE","UPDATE","ALTER","DROP","CREATE","START"};
        String queryTable = null;
        queryString = queryString.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = queryString.split(" ");
        String statementType;
        
        if(Arrays.asList(queryLanguageTokens).contains(queryTokens[0].toUpperCase())) {
            statementType = queryTokens[0].toUpperCase();
            
            switch(statementType) {
                case "SELECT" : if(queryTokens[1] == "*"){
                                    queryTable = queryTokens[3];
                                } else {
                                    String subStr = queryString.substring(queryString.toUpperCase().indexOf("FROM")+4,queryString.toUpperCase().indexOf("WHERE"));
                                    queryTable = subStr.trim();
                                }
                                break;
                case "INSERT" :     if(queryTokens[2].contains("(")){
                                        queryTable = queryTokens[2].substring(0,queryTokens[2].indexOf("(")).trim();
                                    } else {
                                        queryTable = queryTokens[2].trim();
                                    }
                                    break;
                case "DELETE" : queryTable = queryTokens[2];break;
                case "UPDATE" : queryTable = queryTokens[1];break;
                case "ALTER"  : queryTable = queryTokens[2];break;
                case "DROP"   : queryTable = queryTokens[2];break;
            }
        }
        
        return queryTable;
    }
}

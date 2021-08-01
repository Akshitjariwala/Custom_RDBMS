package queryProcessor;

import Transaction.TransactionHandler;
import Transaction.TransactionQueue;
import java.io.*;
import java.util.Date;


public class Update {
    
    public static TransactionHandler transactionHandler;
    
    public static void main(String[] args) throws IOException {
        String sqlQuery = "update employees set department_ID = 1200 where employee_Id = 1000 AND username = akshit";
        sqlQuery = sqlQuery.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlQuery.split(" ");
        String dataName = "database1";
        String tableName = "user_data.txt";
        String currentDirectory = System.getProperty("user.dir");
        System.out.println(currentDirectory);
        performUpdate(dataName,tableName ,sqlQuery,queryTokens);
    }
    
    public static void performUpdate(String databaseName, String tableName,String sqlQuery,String[] queryTokens) throws IOException {
    
        transactionHandler = new TransactionHandler();
        String tablePath = databaseName+"/"+tableName;
        
        if(transactionHandler.checkLock(tablePath)){
            // to queue.
        } else {
            // fetch from queue.
           
            TransactionQueue.transactionQueue.add(sqlQuery);
            while(TransactionQueue.transactionQueue.size() > 0){
                transactionHandler.lockTable(tablePath);
                /* Perform Update logic here
                 unlock the table*/
                String query = TransactionQueue.transactionQueue.peek();
                try{
                    System.out.println("Updating data into database.");
                    System.out.println("Executing Query : "+query);
                    Thread.sleep(10000);
                    transactionHandler.unlockTable(tablePath,tableName);
                    System.out.println("Time Now: "+ new Date());
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                
            }
        }
    }
}
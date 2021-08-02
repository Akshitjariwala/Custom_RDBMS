package queryProcessor;

import Transaction.TransactionHandler;
import Transaction.TransactionQueue;
import java.io.*;
import java.util.Date;
import java.util.List;


public class Update {
    
    public static TransactionHandler transactionHandler;
    public static TransactionQueue transactionQueue = new TransactionQueue();
    
    public static void main(String[] args) throws IOException {
        String sqlQuery = "update employees set department_ID = 1200 where employee_Id = 1000 AND username = akshitjariwala";
        //String sqlQuery = "update employees set department_ID = 900 where employee_Id = 1000 AND username = jariwala";
        sqlQuery = sqlQuery.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlQuery.split(" ");
        String dataName = "database1";
        String tableName = "user_data.txt";
        performUpdate(dataName,tableName ,sqlQuery,queryTokens);
    }
    
    public static void performUpdate(String databaseName, String tableName,String sqlQuery,String[] queryTokens) throws IOException {
        
        transactionHandler = new TransactionHandler();
        String tablePath = databaseName+"/"+tableName;
        List<String> queueList;
        
        if(transactionHandler.checkLock(tablePath)) {
            transactionQueue.AddToQueue(sqlQuery);
            System.out.println("Your Query : "+sqlQuery);
            System.out.println("Waiting for other transactions to complete. Your query will be executed.");
        } else {
            System.out.println("Table is not locked.");
            transactionQueue.AddToQueue(sqlQuery);
            int i=0;
            queueList = transactionQueue.fetchFromQueue();
            while(i < queueList.size()){
                try {
                    transactionHandler.lockTable(tablePath);
                    /********Table is locked*********/
                    System.out.println("Updating data into database.");
                    /* Add update query logic here */
                    System.out.println("Executing Query : " + queueList.get(i));
                    /********Removing Query from queue*******************/
                    transactionQueue.removeFromQueue(queueList.get(i));
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                } finally {
                    transactionHandler.unlockTable(tablePath, tableName);
                    System.out.println("Time Now: " + new Date());
                    /********Table is unlocked*********/
                }
                System.out.println("Queue is empty now.\n");
                queueList = transactionQueue.fetchFromQueue();
            }
        }
    }
}
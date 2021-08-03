package Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.lang.Math;

public class TransactionLog {
    
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();
    
    public void createTransactionLog(String databaseName,String tableName, List<String> columnList, List<String> oldValues, List<String> newValues){
        int randomNumber = (int)(Math.random()*(9999-1000+1)+1000);
        String transactionID = "T"+randomNumber;
        String printPattern = "%15s |%15s |%15s |%15s |%20s |%20s |%20s |%20s%n";
        String currentDirectory = System.getProperty("user.dir");
        String fileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/transactionLogs.txt";
        int length = oldValues.size();
        
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(fileName,true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            printWriter.printf(printPattern,"Transaction ID","Operation","Database","Table","Attribute","Before","After","Timestamp");
            printWriter.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

            fileWriter = new FileWriter(file,true);
            
            System.out.println("Log creation started.");
                
            for(int i=0;i<length;i++){
                printWriter.printf(printPattern,transactionID,"UPDATE",databaseName,tableName,columnList.get(i),oldValues.get(i),newValues.get(i),formatter.format(date));
            }
            
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
            
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

package Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class TransactionQueue {
    
    public static Queue<String> transactionQueue = new LinkedList<>();
    public static String currentDirectory = System.getProperty("user.dir");
    
    public void AddToQueue(String tablePath){
        try {
            String oldFileName = currentDirectory+"/appdata/transactionFiles/";
            BufferedWriter writer = new BufferedWriter(new FileWriter(oldFileName, true));
            writer.write(tablePath+"\t||\tlocked");
            writer.write('\n');
            writer.close();
            System.out.println("table is locked");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

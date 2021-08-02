package Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TransactionQueue {

    public static String currentDirectory = System.getProperty("user.dir");
    public String fileName = currentDirectory+"/appdata/transactionFiles/queryQueueFile";
    
    public void AddToQueue(String updateQuery){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(updateQuery);
            writer.write('\n');
            writer.close();
            System.out.println("Query Added to the queue");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> fetchFromQueue(){
        List<String> queryList = new ArrayList<>();
        String query;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while((query = reader.readLine()) != null){
                queryList.add(query);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return queryList;
    }
    
    public void removeFromQueue(String query){
        String newFileName = currentDirectory+"/appdata/transactionFiles/tempFile.txt";
        File oldFile = new File(fileName);
        File newFile = new File(newFileName);
        try {
            FileReader fileReader = new FileReader(oldFile);
            BufferedReader reader = new BufferedReader(new FileReader(oldFile));
            FileWriter fileWriter = new FileWriter(newFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            String queryString;
            
            while((queryString = reader.readLine()) != null){
                if(!(queryString.equals(query))){
                    printWriter.println(queryString);
                }
            }
            
            printWriter.flush();
            printWriter.close();
            fileReader.close();
            reader.close();
            fileWriter.close();
            bufferedWriter.close();
            
            oldFile.delete();
            
            File newName = new File(fileName);
            newFile.renameTo(newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

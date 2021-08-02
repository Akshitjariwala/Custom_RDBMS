package Transaction;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

public class TransactionHandler {
    
    public String databaseName;
    
    public TransactionHandler(String databaseName){
        this.databaseName = databaseName;
    }
    
    String currentDirectory = System.getProperty("user.dir");
    public String fileName = "fileLockLogs";
    
    public void lockTable(String tablePath){
        try {
            String oldFileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
            File file = new File(oldFileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(oldFileName, true));
            if(file.exists()){
                writer.write(tablePath+"\t||\tlocked");
                writer.write('\n');
                writer.close();
                System.out.println("Table is locked.");
                writer.close();
            } else {
                writer.write("TableName\t||\tStatus");
                writer.write('\n');
                writer.write(tablePath+"\t||\tlocked");
                writer.write('\n');
                writer.close();
                System.out.println("Table is locked.");
                writer.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean checkLock(String tableName) throws IOException {
        Boolean locked = false;
        String logs = null;
        Map<String,String> fileLogMap = new HashMap<>();
        String oldFileName  = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
        BufferedReader reader = new BufferedReader(new FileReader(oldFileName));
        File file = new File(oldFileName);
        
        if(file.exists()){
            while((logs = reader.readLine()) != null){
                String[] tableMetaData = logs.split("\\|\\|");
                fileLogMap.put(tableMetaData[0].trim(),tableMetaData[1].trim());
            }
    
            System.out.println("Checking Table for locks.");
    
            for(Map.Entry<String,String> entry : fileLogMap.entrySet())
            {
                if(entry.getKey().equals(tableName)){
                    if(entry.getValue().equals("locked"))  {
                        System.out.println("Table is Locked. Your Transaction will be executed after other Transaction finishes execution.");
                        locked = true;
                    }
                } else {
                    continue;
                }
            }
            reader.close();
        } else {
            System.out.println("No Lock File Exists.");
        }
        
        return locked;
    }
    
    public Boolean unlockTable(String tablePath,String tableName) throws IOException {
        Boolean locked = false;
        String oldFileName  = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
        Map<String,String> fileLogMap = new HashMap<>();
        removeLock(tablePath);
        System.out.println("Table successfully unlocked.");
        return locked;
    }
    
    public void removeLock(String tablePath) throws IOException {
        try {
            String  oldFileName  = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+fileName;
            String newFileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/"+"tempFile.txt";
            File oldFile = new File(oldFileName);
            File newFile = new File(newFileName);
            FileReader fileReader = new FileReader(oldFileName);
            BufferedReader reader = new BufferedReader(new FileReader(oldFileName));
            FileWriter fileWriter = new FileWriter(newFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            String logs = null;
            
            while((logs = reader.readLine()) != null){
                String[] tableMetaData = logs.split("\\|\\|");
                if(!tableMetaData[0].trim().equals(tablePath)){
                    printWriter.println(tableMetaData[0].trim()+"\t||\t"+tableMetaData[1].trim());
                }
            }
            
            printWriter.flush();
            printWriter.close();
            reader.close();
            bufferedWriter.close();
            fileReader.close();
            fileWriter.close();
            
            oldFile.delete();
            
            File newName = new File(oldFileName);
            newFile.renameTo(newName);
            
        } catch(NoSuchFileException e)
        {
            System.out.println(e);
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    
}


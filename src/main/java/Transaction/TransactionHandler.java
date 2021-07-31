package Transaction;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TransactionHandler {
    
    public static String fileLockLogsPath = "D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/transactionFiles/fileLockLogs";
    
    public void lockFile(String tablePath){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileLockLogsPath, true));
            writer.write(tablePath+"\t||\tlocked");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean checkLock() {
        Boolean locked = false;
    
    
        return locked;
    }
    
    public Boolean unlockFile(String tablePath,String tableName) throws IOException {
        Boolean locked = false;
        String logs = null;
        Map<String,String> fileLogMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileLockLogsPath));
        Map<String,String> newFileLogMap = new HashMap<>();
        
        while((logs = reader.readLine()) != null){
            String[] tableMetaData = logs.split("\\t||\\t");
            fileLogMap.put(tableMetaData[0],tableMetaData[1]);
        }
        
        for(Map.Entry<String,String> entry : fileLogMap.entrySet())
        {
            if(entry.getKey().equals(tableName)){
                continue;
            } else {
                newFileLogMap.put(entry.getKey(),entry.getValue());
            }
        }
        reader.close();
    
        copyFile(newFileLogMap);
        
        return locked;
    }
    
    
    public void copyFile(Map<String,String> newMap) throws IOException {
        try {
            Files.deleteIfExists(Paths.get("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/transactionFiles/fileLockLogs"));
            FileWriter writer = new FileWriter("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/transactionFiles/fileLockLogs");
            for(Map.Entry<String,String> entry : newMap.entrySet())
            {
                writer.write(entry.getKey()+"\t||\t"+entry.getValue());
            }
            writer.close();
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


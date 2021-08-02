package Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionQueue {
    
    public String databaseName;
    public String currentDirectory;
    public String fileName;
    public String tokenFileName;
    
    public TransactionQueue(String databaseName) {
        this.databaseName = databaseName;
        this.currentDirectory = System.getProperty("user.dir");
        this.fileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/queryQueueFile";
        this.tokenFileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/tokenQueueFile";
    }
    
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
    
    public void AddToQueueTokens(Map<String, Object> validationTokens){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tokenFileName, true));
            writer.write(String.valueOf(validationTokens));
            writer.write('\n');
            writer.close();
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
    
    public List<Map<String, Object>> fetchFromTokenQueue(){
        List<Map<String, Object>> tokenList = new ArrayList<>();
        String query;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tokenFileName));
            while((query = reader.readLine()) != null){
                tokenList.add(convertStringToMap(query));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return tokenList;
    }
    
    public void removeFromQueue(String query){
        String newFileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/tempFile.txt";
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
    
    public void removeFromQueueTokens(Map<String, Object> validationTokens){
        String newFileName = currentDirectory+"/appdata/database/"+databaseName+"/transactionFiles/tempFile.txt";
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
                if(!(queryString.equals(String.valueOf(validationTokens)))){
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
    
    
    public static Map<String, Object> convertStringToMap(String stringTOMap){
        Map<String, Object> myMap = new HashMap<String, Object>();
        String s = "{setColumns=[user_name=UpdatedMukesh1, user_contact=Updated55662233111], whereColumnList=[user_contact, 5566223311], setColumnList=[user_contact, Updated55662233111], databaseName=database1, isValid=true, whereList=[user_name=Mukesh, user_contact=5566223311], tableName=user_data}";
        String[] pairs = s.split("],");
        String pair=null;
        
        for (int i=0;i<pairs.length;i++) {
            if(i == 0){
                pair = pairs[i].substring(1)+"]";
                String[] keyValue = pair.split("=");
                myMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
            } else if(i == pairs.length-1){
                pair = pairs[i].substring(0,pairs.length-2);
                String[] keyValue = pair.split("=");
                myMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
            } else {
                String[] keyValue = pair.split("=");
                myMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
            }
        }
        
        return myMap;
    }
    
    
}

package sqlDump;

import dataDictionary.DataDictionary;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SqlDump {
    
    public static String currentDirectory = System.getProperty("user.dir");
    public static String fileName = currentDirectory+"/appdata/database/";
    public DataDictionary dataDictionary = new DataDictionary();
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();
    
    public void storeCreateQuery(String databaseName,String createQuery,String tableName) {
        
        String createQueryFileName = fileName;
        boolean fileExists;
        createQueryFileName = createQueryFileName+databaseName+"/"+databaseName+"_"+"createQueryFile.txt";
        File queryFile = new File(createQueryFileName);
        fileExists = queryFile.exists();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(createQueryFileName, true));
            if(fileExists){
                writer.write(tableName+"\t|\t"+createQuery);
                writer.write(System.lineSeparator());
            } else {
                writer.write("TABLE\t|\tQUERY");
                writer.write('\n');
                writer.write(tableName+"\t|\t"+createQuery);
                writer.write('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getSQLDump(String databaseName){
        String dateStr;
        dateStr = formatter.format(date);
        dateStr = dateStr.replaceAll("\\s+","_").replaceAll("/","_").replaceAll(":","_");
        String createQueryFileName = fileName;
        createQueryFileName = createQueryFileName+"/sqlDumpFiles/"+databaseName+"_"+dateStr+".sql";
        File queryFile = new File(createQueryFileName);
        List<String> tempList;
        
        List<String> tableList = new ArrayList<>();
        try {
            Map<String, List<String>> dictionaryMap = dataDictionary.getDataDictionary(databaseName);
            
            for(Map.Entry<String,List<String>> entry : dictionaryMap.entrySet()){
                tableList.add(entry.getKey());
            }
            
            for(int i=0;i<tableList.size();i++){
                tempList = fetchCreateQueries(databaseName,tableList.get(i));
                // logic to write in file.
                tempList.clear();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> fetchCreateQueries(String databaseName, String tableName) {
        String createQueryFileName = fileName;
        boolean fileExists;
        List<String> tableSQLDump  = new ArrayList<>();
        createQueryFileName = createQueryFileName+databaseName+"/"+databaseName+"_"+"createQueryFile.txt";
        File queryFile = new File(createQueryFileName);
        fileExists = queryFile.exists();
        Map<String,String> queryMap = new HashMap<>();
        String record;
        String queryTOReturn = null;
        if(fileExists){
            try {
                BufferedReader queryReader = new BufferedReader(new FileReader(createQueryFileName));
                while((record = queryReader.readLine()) != null){
                    String[] entry = record.split("\t|\t");
                    queryMap.put(entry[0],entry[1]);
                }
                
                for(Map.Entry<String,String> entry : queryMap.entrySet()){
                    if(entry.getKey().equals(tableName)){
                        queryTOReturn = entry.getValue();
                        tableSQLDump.add(queryTOReturn);
                    }
                }
    
                List<String> tempList = fetchInsertQueries(databaseName,tableName);
                
                for(int i=0;i<tempList.size();i++){
                    tableSQLDump.add(tempList.get(i));
                }
                
                queryReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No create query.");
        }
        return tableSQLDump;
    }
    
    public static List<String> fetchInsertQueries(String databaseName, String tableName){
        List<String> insertQueryList  = new ArrayList<>();
        String insertQueryTablePath = fileName;
        insertQueryTablePath = insertQueryTablePath + databaseName +"/" +tableName+".txt";
        File queryFile = new File(insertQueryTablePath);
        String insertQuery;
        try {
        if(queryFile.exists()){
            BufferedReader queryReader = new BufferedReader(new FileReader(insertQueryTablePath));
            while((insertQuery = queryReader.readLine()) != null){
                if(!(insertQuery.matches("TABLE\t|\tQUERY"))){
                    insertQuery = insertQuery.replaceAll("\\s+||\\s+",",");
                    insertQuery = "INSERT INTO "+tableName+" VALUES ("+insertQuery+" )";
                    insertQueryList.add(insertQuery);
                }
            }
        } else {
            System.out.println("ERROR: Table Does Not Exists.");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insertQueryList;
    }
}

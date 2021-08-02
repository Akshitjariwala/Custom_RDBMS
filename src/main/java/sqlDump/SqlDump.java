package sqlDump;

import dataDictionary.DataDictionary;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SqlDump {
    
    public static String currentDirectory = System.getProperty("user.dir");
    public static String fileName = currentDirectory+"/appdata/database/";
    public static DataDictionary dataDictionary = new DataDictionary();
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
    
    public static Boolean getSQLDump(String databaseName){
        String dateStr;
        dateStr = formatter.format(date);
        dateStr = dateStr.replaceAll("\\s+","_").replaceAll("/","_").replaceAll(":","_");
        String createQueryFileName = fileName;
        createQueryFileName = createQueryFileName+"/sqlDumpFiles/"+databaseName+"_"+dateStr+".sql";
        List<String> tempList;
        Boolean sqlDumpGeneration;
        
        List<String> tableList = new ArrayList<>();
        try {
            Map<String, List<String>> dictionaryMap = dataDictionary.getDataDictionary(databaseName);
            
            for(Map.Entry<String,List<String>> entry : dictionaryMap.entrySet()){
                tableList.add(entry.getKey());
            }
    
            File file = new File(createQueryFileName);
            
            if(file.createNewFile()){
                FileWriter fileWriter = new FileWriter(createQueryFileName);
                fileWriter.write("-----------------------------------------------SQL DUMP FOR "+databaseName+"--------------------------------------------");
                for(int i=0;i<tableList.size();i++){
                    tempList = fetchCreateQueries(databaseName,tableList.get(i));
                    fileWriter.write(System.getProperty( "line.separator" ));
                    for(int j=0;j<tempList.size();j++){
                        fileWriter.write(tempList.get(j));
                        fileWriter.write('\n');
                    }
                    tempList.clear();
                }
                fileWriter.close();
                sqlDumpGeneration = true;
            } else {
                System.out.println("SQL Dump Creation Failed.");
                sqlDumpGeneration = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            sqlDumpGeneration = false;
        }
        
        return sqlDumpGeneration;
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
        String queryTOReturn;
        if(fileExists){
            try {
                BufferedReader queryReader = new BufferedReader(new FileReader(createQueryFileName));
                while((record = queryReader.readLine()) != null) {
                    String[] entry = record.split("\\|");
                    queryMap.put(entry[0].trim(),entry[1].trim());
                }
                
                for(Map.Entry<String,String> entry : queryMap.entrySet()){
                    if(entry.getKey().equals(tableName)) {
                        queryTOReturn = entry.getValue()+";";
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
    
    public static List<String> fetchInsertQueries(String databaseName, String tableName) {
        List<String> insertQueryList  = new ArrayList<>();
        String insertQueryTablePath = fileName;
        insertQueryTablePath = insertQueryTablePath + databaseName +"/" +tableName+".txt";
        File queryFile = new File(insertQueryTablePath);
        String insertQuery;
        try {
        if(queryFile.exists()){
            BufferedReader queryReader = new BufferedReader(new FileReader(insertQueryTablePath));
            int flag = 0; // To remove columns from insert query
            while((insertQuery = queryReader.readLine()) != null){
                if(flag>0){
                    insertQuery = insertQuery.replaceAll("\\s*\\|\\|\\s*","','");
                    insertQuery = "INSERT INTO "+tableName+" VALUES ('"+insertQuery+"');";
                    insertQueryList.add(insertQuery);
                }
                flag++;
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

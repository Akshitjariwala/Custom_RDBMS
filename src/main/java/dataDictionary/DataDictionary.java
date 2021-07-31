package dataDictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDictionary {
        public  Map<String, List<String>> getDataDictionary(String databaseName) throws IOException
        {
        String dataDictionary = "data_dictionary.txt";
        File dataDictionaryFile = new File("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName+"/"+dataDictionary);
        Map<String,List<String>> tableDictionary = new HashMap<>();
        FileInputStream inputStream = new FileInputStream("D:/Materiel/Database Analytics/Project/csci-5408-s2021-group-19/appdata/database/"+databaseName+"/"+dataDictionary);
        BufferedReader bufferStream = new BufferedReader(new InputStreamReader(inputStream));
        String tableLine;
        String table_name;
        int separetorIndex;
        String dictionarySubString;
        
        if(dataDictionaryFile.exists()){
            while((tableLine = bufferStream.readLine()) != null){
                List<String> columnDefinitionList = new ArrayList<>();
                separetorIndex = tableLine.indexOf("||");
                table_name = tableLine.substring(0,separetorIndex).trim();
                if(!table_name.equals("TABLE NAME")){ // Removing first row.
                    dictionarySubString = tableLine.substring(separetorIndex+2,tableLine.length()).trim();
                    String[] columnDefinitionArray = dictionarySubString.split("\t|\t");
                    for(int i=0;i<columnDefinitionArray.length;i++){
                        if(!columnDefinitionArray[i].equals("|")){
                            columnDefinitionList.add(columnDefinitionArray[i]);
                        }
                    }
                    tableDictionary.put(table_name,columnDefinitionList);
                }
            }
        } else {
            System.out.println("Data Dictionary Does not exists.");
        }
        
        return tableDictionary;
    }
    
}



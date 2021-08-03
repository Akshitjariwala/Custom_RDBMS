package queryProcessor;
import dataDictionary.DataDictionary;
import queryValidator.QueryValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Insert {

    //inserting data into the table
    public void insertData(Map<String, Object> queryTokens) throws IOException {

        List<Object> columnNames = new ArrayList<>();
        columnNames = (List<Object>) queryTokens.get("columnNames");
        List<Object> columnValues = new ArrayList<>();
        columnValues = (List<Object>) queryTokens.get("columnValues");
        String tableName = queryTokens.get("tableName").toString();
        List<String> columnList = getPrimaryColumn(tableName);
        String pKTempColumn = null;
        String tablePath = "appdata/database/database1/" + tableName + ".txt";
        for(int i = 0; i < columnList.size(); i++){
            System.out.println("Column List: " + columnList.get(i));
            if( columnList.get(i).contains("PK")) {
                pKTempColumn = columnList.get(i);
            }
        }
        String primaryKeyColumn = null;
        primaryKeyColumn = pKTempColumn.split("\\(")[1].replace(")","");
        System.out.println(primaryKeyColumn);

        int primaryKeyPosition = 0;
        for(int i = 0; i < columnNames.size(); i++) {
            if(columnNames.get(i).equals(primaryKeyColumn))
                primaryKeyPosition = i;
        }

        String primaryKeyValue = (String) columnValues.get(primaryKeyPosition);

        Map<String,List<String>> tableDictionary = new HashMap<>();
        FileInputStream inputStream = new FileInputStream(tablePath);
        BufferedReader bufferStream = new BufferedReader(new InputStreamReader(inputStream));
        String tableLine;
        int i = 1;
        int pos =0;
        boolean dupStatus = false;

        while((tableLine = bufferStream.readLine()) != null) {
            String[] columnsSplit = null;
            columnsSplit = tableLine.split("\\t||\\t");
            if (i == 1) {
                for (int j = 0; j < columnsSplit.length; j++) {
                    if (columnsSplit[j].contains(primaryKeyColumn)) {
                        pos = j;
                        i++;
                    }
                }
            }
            if (columnsSplit[pos].equals(primaryKeyValue)) {
                System.out.println("Duplicate Error");
                dupStatus = true;
            }
        }

        if(dupStatus == false){

            File tablefile = new File(tablePath);
            if (tablefile.exists()) {
                FileWriter tablefileWriter = new FileWriter(tablefile, true);
                tablefileWriter.append("\n");

                for (int k = 0; k < columnValues.size(); k++) {
                    if (!(k == columnValues.size() - 1)) {
                        tablefileWriter.append(columnValues.get(k).toString() + "\t" + "||" + "\t");
                        System.out.println(columnValues.get(k).toString());
                    } else {
                        tablefileWriter.append(columnValues.get(k).toString());
                        System.out.println(columnValues.get(k).toString());
                    }
                }
                tablefileWriter.flush();
                tablefileWriter.close();
            } else {
                System.out.println("Table Does Not Exist");
            }
        }
    }


    public List<String> getPrimaryColumn(String tableName) throws IOException {
        DataDictionary dataDictionary = new DataDictionary();
        Map<String, List<String>> tableDictionary = dataDictionary.getDataDictionary(QueryValidator.databaseName);
        List<String> attributeList = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        String subString = "";
        String columnName = "";

        if (tableDictionary.containsKey(tableName)) {
            attributeList = tableDictionary.get(tableName);
            for(int k=0;k<attributeList.size();k++){
                String[] columnNameListTemp = attributeList.get(k).split("\\s+");
                for(int i=0;i<columnNameListTemp.length;i++) {
                    columnName = columnNameListTemp[0];
                    columnNameList.add(columnName);
                    break;
                }
            }
        }
        return columnNameList;
    }
}

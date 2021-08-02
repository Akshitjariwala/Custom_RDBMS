package queryProcessor;
import dataDictionary.DataDictionary;
import queryValidator.QueryValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Insert {
    public void insertData(Map<String, Object> queryTokens) throws IOException {

        List<Object> colNames = new ArrayList<>();
        colNames = (List<Object>) queryTokens.get("columnNames");
        List<Object> colValues = new ArrayList<>();
        colValues = (List<Object>) queryTokens.get("columnValues");
        String tableName = queryTokens.get("tableName").toString();
        List<String> columnList = getPrimaryColumn(tableName);
        String pkColtemp = null;
        String tablepath = "appdata/database/database1/" + tableName + ".txt";
        for(int i = 0; i < columnList.size(); i++){
            System.out.println("Column List: " + columnList.get(i));
            if( columnList.get(i).contains("PK")) {
                pkColtemp = columnList.get(i);
            }
        }
        String pkcol = null;
        pkcol = pkColtemp.split("\\(")[1].replace(")","");
        System.out.println(pkcol);

        int pkPos = 0;
        for(int i = 0; i < colNames.size(); i++) {
            if(colNames.get(i).equals(pkcol))
                pkPos = i;
        }

        String pkVal = (String) colValues.get(pkPos);

        //File tableFile = new File(currentDirectory+"/appdata/database/"+databaseName+"/"+dataDictionary);
        Map<String,List<String>> tableDictionary = new HashMap<>();
        FileInputStream inputStream = new FileInputStream(tablepath);
        BufferedReader bufferStream = new BufferedReader(new InputStreamReader(inputStream));
        String tableLine;
        int i = 1;
        int pos =0;
        boolean dupStatus = false;

        while((tableLine = bufferStream.readLine()) != null) {
            String[] colsSplit = null;
            colsSplit = tableLine.split("\\t||\\t");
            if (i == 1) {
                for (int j = 0; j < colsSplit.length; j++) {
                    if (colsSplit[j].contains(pkcol)) {
                        pos = j;
                        i++;
                    }
                }
            }
            if (colsSplit[pos].equals(pkVal)) {
                System.out.println("Duplicate Error");
                dupStatus = true;
            }
        }

        if(dupStatus == false){

            File tablefile = new File(tablepath);
            if (tablefile.exists()) {
                FileWriter tablefileWriter = new FileWriter(tablefile, true);
                tablefileWriter.append("\n");

                for (int k = 0; k < colValues.size(); k++) {
                    if (!(k == colValues.size() - 1)) {
                        tablefileWriter.append(colValues.get(k).toString() + "\t" + "||" + "\t");
                        System.out.println(colValues.get(k).toString());
                    } else {
                        tablefileWriter.append(colValues.get(k).toString());
                        System.out.println(colValues.get(k).toString());
                    }
                }
                tablefileWriter.flush();
                tablefileWriter.close();
            } else {
                System.out.println("Table Does Not Exist");
            }
        }
    }
    //System.out.println("debug3");



    public List<String> getPrimaryColumn(String tableName) throws IOException {
        DataDictionary dd = new DataDictionary();
        Map<String, List<String>> tableDictionary = dd.getDataDictionary(QueryValidator.databaseName);
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

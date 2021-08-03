package queryProcessor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Create {
    
    public static String currentDirectory = System.getProperty("user.dir");

    public void createDataDictionary(Map<String,Object> queryTokens) throws IOException {
        List<Object> columns = new ArrayList<>();
        columns = (List<Object>) queryTokens.get("columnArray");
        String tableName = queryTokens.get("tableName").toString();
        String ddFilePath = currentDirectory+"/appdata/database/database1/data_dictionary.txt";
        File ddFile = new File(ddFilePath);
        if (ddFile.exists()) {
            FileWriter tablefileWriter = new FileWriter(ddFile, true);
            tablefileWriter.append("\n" + tableName + " ||");
            writeDataDictionary(columns,tablefileWriter);
            tablefileWriter.flush();
            tablefileWriter.close();
        }
        else {
            FileWriter tablefileWriter = new FileWriter(ddFile);
            tablefileWriter.append("\n" + tableName + " ||");
            tablefileWriter.append("TABLE NAME\t||\tCOLUMNS");
            writeDataDictionary(columns,tablefileWriter);
            tablefileWriter.flush();
            tablefileWriter.close();
        }
    }

    public void writeDataDictionary(List<Object> tableColumns, FileWriter tablefileWriter) throws IOException {
        for(int i = 0; i < tableColumns.size(); i++){
            String eachcol = tableColumns.get(i).toString();
            if(eachcol.contains("PRIMARY_KEY")){
                tablefileWriter.append("\t" + eachcol.replace("PRIMARY_KEY", "PK").replace(" ", "") + "\t" + "|");
            }
            else if (eachcol.contains("CONSTRAINT")) {
                String[] splitted = eachcol.split(" ");
                String foreignKey = "FK";
                String col1 = splitted[3].replace("(","").replace(")","");
                String[] references = splitted[5].split("\\(");
                String refTable = references[0];
                String refCol = references[1].replace(")", "");
                tablefileWriter.append("\t"+ foreignKey + "(" + col1 + "," + refCol+ ","  + refTable + ")");
            }
            else {
                tablefileWriter.append("\t" + eachcol + "\t"+ "|");
            }
        }
    }

    public void createTable(Map<String,Object> queryTokens) throws IOException {
        List<Object> column = new ArrayList<>();
        column = (List<Object>) queryTokens.get("columnArray");
        String tableName = queryTokens.get("tableName").toString();
        System.out.println(tableName);
        String tableFilePath = "appdata/database/database1/" + tableName + ".txt";
        String ddFilePath = "appdata/database/database1/data_dictionary.txt";
        File tableFile = new File(tableFilePath);
        if (tableFile.exists()) {
            System.out.println("Table Already Exists");
        }
        else {
            FileWriter tablefileWriter = new FileWriter(tableFile);
            int listSize = 0;
            if( column.get(column.size()-1).toString().contains("PRIMARY_KEY"))
                listSize = column.size()-1;
            else if( column.get(column.size()-1).toString().contains("CONSTRAINT"))
                listSize = column.size()-2;
            else
                listSize = column.size();

            for(int i = 0; i < listSize; i++){
                String singleCol = column.get(i).toString();
                String[] splitted = singleCol.split(" ");
                if(!(i== listSize-1))
                    tablefileWriter.append(splitted[0] + "\t" + "||" + "\t");
                else
                    tablefileWriter.append(splitted[0]);
            }
            tablefileWriter.flush();
            tablefileWriter.close();
        }
    }
}
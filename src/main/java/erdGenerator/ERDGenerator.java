package erdGenerator;
import dataDictionary.DataDictionary;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ERDGenerator {
    
    public String currentDirectory = System.getProperty("user.dir");
    DataDictionary dictionary = new DataDictionary();
    Map<String, List<String>> dictionaryMap = new HashMap<>();
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();
    
    public Boolean createERD(String database) throws IOException {
        String dateStr;
        dateStr = formatter.format(date);
        dateStr = dateStr.replaceAll("\\s+","_").replaceAll("/","_").replaceAll(":","_");
        String fileName = dateStr+"_ERD_"+database;
        File file = new File(currentDirectory+"/appdata/ERD/"+fileName);
        
        if(file.createNewFile())
        {
            FileWriter writer = new FileWriter(currentDirectory+"/appdata/ERD/"+fileName);
            writer.write(System.getProperty( "line.separator" ));
            dictionaryMap = dictionary.getDataDictionary(database);
            // last to second column PK and last column FK
            writer.write("----------------------------------------------------------------------------------------------------------------------------------------------------------");
            writer.write(System.getProperty( "line.separator" ));
            writer.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEntity Relationship Diagram\t\t\t\t\t");
            writer.write(System.getProperty( "line.separator" ));
            writer.write("----------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            writer.write(System.getProperty( "line.separator" ));
            
            writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s |%15s |%15s%n","","Column Name","Data Type","Constraints","Key","Key","Reference Table","Reference Column"));
            for (Map.Entry<String,List<String>> table : dictionaryMap.entrySet())
            {
                List<String> columnList;
                columnList = table.getValue();
                writer.write("----------------------------------------------------------------------------------------------------------------------------------------------------------");
                writer.write(System.getProperty( "line.separator" ));
                writer.write("TABLE NAME : ["+table.getKey()+"]");
                writer.write(System.getProperty( "line.separator" ));
                writer.write("----------------------------------------------------------------------------------------------------------------------------------------------------------");
                writer.write(System.getProperty( "line.separator" ));
                Boolean pkFlag = checkPrimaryKey(columnList);
                Boolean fkFlag = checkForeignKey(columnList);
            
                if(pkFlag == true && fkFlag == true) {
                    String primaryKeyString = columnList.get(columnList.size()-2);
                    String primaryKey = primaryKeyString.substring(primaryKeyString.indexOf("(")+1,primaryKeyString.indexOf(")")); // Fetching Primary key column from metadata.
                    String foreignKeyString = columnList.get(columnList.size()-1);
                    String foreignKeyColumnSameTable = foreignKeyString.substring(foreignKeyString.indexOf("(")+1,foreignKeyString.indexOf(","));
                    for(int i=0;i<columnList.size()-2;i++){ //To iterate loop only till PK.
                        Boolean primaryFlag = false;
                        Boolean foreignKeyFlag = false;
                        String[] metaData = columnList.get(i).split("\\s+");
                        if(metaData.length > 2){
                            List<String> constraintArray = new ArrayList<>();
                            int constraintCounter = 2;
                    
                            // to Store the constraints in an array.
                            for(int j=0;j<metaData.length;j++){
                                constraintArray.add(metaData[constraintCounter]);
                                if(constraintCounter < metaData.length-1) {
                                    constraintCounter++;
                                } else {
                                    break;
                                }
                            }
                    
                            if(primaryKey.equals(metaData[0])){
                                primaryFlag = true;
                            }
                    
                            if(foreignKeyColumnSameTable.equals(metaData[0])) {
                                foreignKeyFlag = true;
                            }
                    
                            String fkTable = getReferenceTable(foreignKeyString);
                            String fkColumn = getReferenceColumn(foreignKeyString);
                    
                            if (primaryFlag == true && foreignKeyFlag == true){
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s |%15s |%15s%n","" ,metaData[0], metaData[1], constraintArray, "PRIMARY KEY", "FOREIGN KEY",fkTable,fkColumn));
                            } else if(primaryFlag == true && foreignKeyFlag ==  false) {
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s%n", "",metaData[0], metaData[1], constraintArray, "PRIMARY KEY", ""));
                            } else if(primaryFlag == false && foreignKeyFlag ==  true) {
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s |%15s |%15s%n", "",metaData[0], metaData[1], constraintArray, "", "FOREIGN KEY",fkTable,fkColumn));
                            } else {
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s%n","",metaData[0],metaData[1],constraintArray,"",""));
                            }
                        } else {
                            writer.write(metaData[0]+"\t\t\t\t"+metaData[1]);
                            writer.write(System.getProperty( "line.separator" ));
                        }
                    }
                } else if(pkFlag == true && fkFlag == false) {
                    String primaryKeyString = columnList.get(columnList.size()-1);
                    String primaryKey = primaryKeyString.substring(primaryKeyString.indexOf("(")+1,primaryKeyString.indexOf(")")); // Fetching Primary key column from metadata.
                    for(int i=0;i<columnList.size()-1;i++){
                        Boolean primaryFlag = false;
                        Boolean foreignKeyFlag = false;
                        String[] metaData = columnList.get(i).split("\\s+");
                        if(metaData.length > 2){
                            List<String> constraintArray = new ArrayList<>();
                            int constraintCounter = 2;
                    
                            // To Store the constraints in an array.
                            for(int j=0;j<metaData.length;j++){
                                constraintArray.add(metaData[constraintCounter]);
                                if(constraintCounter < metaData.length-1){
                                    constraintCounter++;
                                } else {
                                    break;
                                }
                            }
                    
                            if(primaryKey.equals(metaData[0])){
                                primaryFlag = true;
                            }
                    
                            if(primaryFlag){
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s%n", "",metaData[0], metaData[1], constraintArray, "PRIMARY KEY"));
                            } else {
                                writer.write(String.format("%20s %15s |%15s |%30s%n", "",metaData[0], metaData[1], constraintArray));
                            }
                        } else {
                            writer.write(metaData[0]+"\t\t\t\t"+metaData[1]);
                            writer.write(System.getProperty( "line.separator" ));
                        }
                    }
                } else if(pkFlag == false && fkFlag == true) {
                    String foreignKeyString = columnList.get(columnList.size()-1);
                    String foreignKeyColumnSameTable = foreignKeyString.substring(foreignKeyString.indexOf("(")+1,foreignKeyString.indexOf(","));
                    for(int i=0;i<columnList.size()-1;i++){
                        Boolean primaryFlag = false;
                        Boolean foreignKeyFlag = false;
                        String[] metaData = columnList.get(i).split("\\s+");
                        if(metaData.length > 2){
                            List<String> constraintArray = new ArrayList<>();
                            int constraintCounter = 2;
                    
                            // To Store the constraints in an array.
                            for(int j=0;j<metaData.length;j++){
                                constraintArray.add(metaData[constraintCounter]);
                                if(constraintCounter < metaData.length-1){
                                    constraintCounter++;
                                } else {
                                    break;
                                }
                            }
                    
                            String fkTable = getReferenceTable(foreignKeyString);
                            String fkColumn = getReferenceColumn(foreignKeyString);
                    
                            if(foreignKeyColumnSameTable.equals(metaData[0])){
                                foreignKeyFlag = true;
                            }
                    
                            if(foreignKeyFlag){
                                writer.write(String.format("%20s %15s |%15s |%30s |%15s |%15s |%15s |%15s%n","", metaData[0], metaData[1], constraintArray, "","FOREIGN KEY",fkTable,fkColumn));
                            } else {
                                writer.write(String.format("%20s %15s |%15s |%30s%n", "",metaData[0], metaData[1], constraintArray));
                            }
                        } else {
                            writer.write(metaData[0]+"\t\t\t\t"+metaData[1]);
                            writer.write(System.getProperty( "line.separator" ));
                        }
                    }
                } else {
                    for(int i=0;i<columnList.size();i++){
                        Boolean primaryFlag = false;
                        Boolean foreignKeyFlag = false;
                        String[] metaData = columnList.get(i).split("\\s+");
                        if(metaData.length > 2){
                            List<String> constraintArray = new ArrayList<>();
                            int constraintCounter = 2;
                    
                            // to Store the constraints in an array.
                            for(int j=0;j<metaData.length;j++){
                                constraintArray.add(metaData[constraintCounter]);
                                if(constraintCounter < metaData.length-1){
                                    constraintCounter++;
                                } else {
                                    break;
                                }
                            }
                            writer.write(String.format("%20s %15s |%15s |%30s%n","",metaData[0],metaData[1],constraintArray));
                        } else {
                            writer.write(metaData[0]+"\t\t\t\t"+metaData[1]);
                        }
                    }
                }
                pkFlag = false;
                fkFlag = false;
                
            }
            writer.close();
            return true;
        } else {
            System.out.println("File Creation Failed.");
            return false;
        }
    
        /*
        SAMPLE GENERATED ERD

        [Person]
        *name
        height
        weight
        +birth_location_id

        [Location]
        *id
        city
        state
        country
         */
    }
    
    public Boolean checkPrimaryKey(List<String> tableDefinition){
        Boolean result;
        String str = tableDefinition.get(tableDefinition.size()-2);
        String pk = str.charAt(0)+""+str.charAt(1);
        String str1 = tableDefinition.get(tableDefinition.size()-1);
        String pk1 = str1.charAt(0)+""+str1.charAt(1);
        if(pk.toUpperCase().equals("PK")){
            result = true;
        } else if (pk1.toUpperCase().equals("PK")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    
    public Boolean checkForeignKey(List<String> tableDefinition){
        Boolean result;
        String str = tableDefinition.get(tableDefinition.size()-2);
        String fk = str.charAt(0)+""+str.charAt(1);
        String str1 = tableDefinition.get(tableDefinition.size()-1);
        String fk1 = str1.charAt(0)+""+str1.charAt(1);
        if(fk.toUpperCase().equals("FK")){
            result = true;
        } else if (fk1.toUpperCase().equals("FK")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    
    public String getReferenceTable(String foreignKeyString){
        String refTable = null;
        String columns = foreignKeyString.substring(foreignKeyString.indexOf("(")+1,foreignKeyString.length()-1);
        String[] columnList = columns.split("\\s*,\\s*");
        refTable = columnList[1];
        return refTable;
    }
    
    public String getReferenceColumn(String foreignKeyString){
        String refColumn = null;
        String columns = foreignKeyString.substring(foreignKeyString.indexOf("(")+1,foreignKeyString.length()-1);
        String[] columnList = columns.split("\\s*,\\s*");
        refColumn = columnList[2];
        return refColumn;
    }
}

package queryProcessor;
import java.io.*;
import java.util.ArrayList;

public class Create {

    //Creating data dictionary
    public static int createDataDictionary(String username, String tablename, ArrayList<String> columns,
                                           ArrayList<String> values) throws IOException {
        try {
            File datadictionary = new File("Data_Dictionary.txt");
            FileWriter fileWriter = new FileWriter(datadictionary, true);
            if (datadictionary.createNewFile()){ //If data dictionary does not exists it will create new one.
                System.out.println("Data Dictionary Created");
            }
            FileReader fileReader = new FileReader(datadictionary);//One user can create only one table with a particular name
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.equalsIgnoreCase(username)) {
                    line = bufferedReader.readLine();  //check the table name with that user
                    if (line.equalsIgnoreCase(tablename)) {
                        System.out.println("User already associated with that table!");
                        return 0;
                    }
                    continue;
                }
                line = bufferedReader.readLine();
            }
            fileWriter.append("UserName : ");
            fileWriter.append(username);    //adding the username first
            fileWriter.append("\n");
            fileWriter.append("TableName : ");
            fileWriter.append(tablename);   //then the table name
            fileWriter.append("\n");

            for (int i = 0; i < columns.size(); i++) {
                fileWriter.append("ColumnName : ");
                fileWriter.append(columns.get(i));   //get the column name
                fileWriter.append(" || Value : " + values.get(i));
                fileWriter.append("\n");
            }
            fileWriter.append("\n");    //empty line to denote end of record
            fileWriter.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Creating Table
    public int createTable(String username, String tablename, ArrayList<String> columns) throws IOException {
        try{
            File file = new File(tablename + ".txt");   //storing data of the table in a separate file
            FileWriter tableFileWriter = new FileWriter(file, true);    //appending table
            if (file.createNewFile()) //create a new one if no table exists for data dictionary
            {
                System.out.println("New table created successfully");
            }
            for (int i = 0; i < columns.size(); i++) {
                tableFileWriter.append(columns.get(i));
                tableFileWriter.append(" ");
                tableFileWriter.append("\n");
            }
            tableFileWriter.append("\n");
            tableFileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
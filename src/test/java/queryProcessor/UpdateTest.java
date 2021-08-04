package queryProcessor;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {

    @Test
    void main() {
    }

    @Test
    void performUpdate() {
    }

    @Test
    void execute() {
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tUpdate Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        // UPDATE user_data set user_name=UpdatedMukesh,user_contact=Updated5566223311 where user_name=Mukesh AND user_contact=5566223311
        // UPDATE FROM user_data WHERE user_email=alex.mark@gmail.com AND user_name=alex AND user_contact=5566223311
        // {setColumns=[user_name=newalex, user_contact=new5566223311], whereColumnList=[user_contact, 5566223311], setColumnList=[user_contact, new5566223311], databaseName=database1, isValid=true, whereList=[user_name=alex, user_contact=5566223311], tableName=user_data}
        Map<String, Object> validationTokens = new HashMap<>();
        validationTokens.put("setColumns", Arrays.asList("user_name=UpdatedMukesh", "user_contact=new5566223311"));
        validationTokens.put("whereList", Arrays.asList("user_name=Mukesh", "user_contact=5566223311"));
        validationTokens.put("tableName", "user_data");
        validationTokens.put("databaseName", "database1");
        System.out.println(validationTokens);
        /*Update.execute(validationTokens);*/
    }

    @Test
    void boeingTest() {
        // update table_c set id=7,Name="Updated Boeing",Company="boeing" where id = 2 AND Name="boeing"
        // Update module received tokens: {setColumns=[id=7, Name=UpdatedBoeing, Company=boeing], whereColumnList=[Name, boeing], setColumnList=[Company, boeing], databaseName=database1, isValid=true, whereList=[id=2, Name=boeing], tableName=table_c}
        System.out.println("\n\n----------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tUpdate Query Test\t\t\t\t\t");
        System.out.println("----------------------------------------------------------------------------");
        Map<String, Object> validationTokens = new HashMap<>();
        // {setColumns=[id=7, Name=UpdatedBoeing, Company=boeing], whereColumnList=[Name, boeing], setColumnList=[Company, boeing], isValid=true, whereList=[id=2, Name=boeing], tableName=table_c}
        validationTokens.put("setColumns", Arrays.asList("Name=Updated Boeing", "Company=boeing"));
        validationTokens.put("whereList", Arrays.asList("id=2", "Name=boeing"));
        validationTokens.put("tableName", "table_c");
        validationTokens.put("databaseName", "database1");
        System.out.println(validationTokens);
        /*Update.execute(validationTokens);*/
    }

    @Test
    void loadTest() {
        List<String> rows = new ArrayList<>();
        System.out.println("RAW DATA");
        try (BufferedReader br = new BufferedReader(new FileReader("appdata/database/database1/table_c.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("> " + line.trim());
                rows.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Invalid table");
        }
        System.out.println("\n\n----------------------------------------------------------------------------");
        // Get number of columns
        int colSize = rows.get(0).split("\\|\\|").length;
        int rowSize = rows.size();
        System.out.println("[" + rowSize + ", " + colSize + "]");
        // insert table into matrix
        String[][] tableMatrix = new String[rowSize][colSize];

        for (int i = 0; i < rowSize; i++) {
            String[] columnValues = rows.get(i).split("\\|\\|");
            for (int j = 0; j < colSize; j++) {
                System.out.print(columnValues[j].trim() + "\t");
                String item = tableMatrix[i][j];
                System.out.print(item + "\t");
                tableMatrix[i][j] = columnValues[j].trim();
            }
            System.out.print("\n");
        }
        // Print table
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                String item = tableMatrix[i][j];
                System.out.print(item + "\t");
            }
            System.out.print("\n");
        }
    }
}
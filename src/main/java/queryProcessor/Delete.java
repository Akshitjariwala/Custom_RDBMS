package queryProcessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Delete {
    private static final String workingDir = System.getProperty("user.dir");

    public static void execute() {
        String databaseName = "database1";
        String tableName = "user_data";
        // Sample query: delete from table_b where column1=100 and column2=akshit and column3 =100
        // TODO: Get tokens
        // tableName
        // condition(s) - columns/values
        // TODO: Open table file
        try (BufferedReader br = new BufferedReader(new FileReader(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: Delete values
        // TODO: Save edits
        // TODO: Close file
    }
}

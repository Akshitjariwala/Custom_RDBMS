package queryProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select {
    private static final String workingDir = System.getProperty("user.dir");

    public static void execute() {
        String databaseName = "database1";
        String tableName = "user_data";
        String[] columnsName = {"user_name", "user_email", "user_contact"};
        Map<String, String> searchTerms = new HashMap<>();
        searchTerms.put("user_name", "alex");
        searchTerms.put("user_email", "alex.mark@gmail.com");
        searchTerms.put("user_contact", "5566223311");
        // Sample query: delete from table_b where column1=100 and column2=akshit and column3 =100
        // TODO: Get tokens
        // tableName
        // condition(s) - columns/values
        // TODO: Open table file
        int i = 0;
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

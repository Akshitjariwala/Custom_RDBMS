package queryProcessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Delete {
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
        // Check for columns names and their index
        List<String> tableCol = Arrays.asList(rows.get(0).split("\t\\|\\|\t"));
        Map<String, Integer> indexOfSearchColumns = new HashMap<>();
        for (String s : columnsName) {
            System.out.println("Search col " + tableCol.indexOf(s) + " for " + searchTerms.get(s));
            indexOfSearchColumns.put(searchTerms.get(s), tableCol.indexOf(s));
        }
        for (String searchTerm : indexOfSearchColumns.keySet()) {
            int columnIndex = indexOfSearchColumns.get(searchTerm);
            System.out.println(columnIndex);
            System.out.println("Searching for " + searchTerm);
            for (String s : rows) {
                String[] splitArray = s.split("\t\\|\\|\t");
                if (splitArray[columnIndex].equals(searchTerm)) {
                    System.out.println("Found " + searchTerm + " at row " + rows.indexOf(s));
                }
            }
        }
        // TODO: Delete values
        // TODO: Save edits
        // TODO: Close file
    }
}

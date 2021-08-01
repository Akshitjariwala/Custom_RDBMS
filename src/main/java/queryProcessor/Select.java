package queryProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
        // Sample query: select column1,column2,column3 from table_a where userid = 100 and user = akshit
        // Sample query: SELECT user_name, user_email, user_contact FROM user_data WHERE user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311
        // TODO: Get tokens
        // tableName
        // condition(s) - columns/values
        System.out.println("SELECT " + Arrays.toString(columnsName).replace('[', '(').replace(']', ')') + " FROM " + tableName + " WHERE " + searchTerms.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));
        // TODO: Open table file
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get number of columns
        int colSize = rows.get(0).split("\\|\\|").length;
        int rowSize = rows.size();
        String[][] tableMatrix = new String[colSize][rowSize];
        for (int i = 0; i < rowSize; i++) {
            // System.out.println(rows.get(i));
            String[] columnValues = rows.get(i).split(("\\|\\|"));
            for (int j = 0; j < colSize; j++) {
                tableMatrix[i][j] = columnValues[j].trim();
            }
        }
        // Match select columns to index number
        Map<String, Integer> indexOfSelectColumns = new HashMap<>();
        Map<String, Integer> indexOfSearchColumns = new HashMap<>();
        for (String s : columnsName) {
            for (int j = 0; j < rowSize; j++) {
                if (tableMatrix[0][j].equals(s)) {
                    // System.out.println("Columns \"" + s + "\" found at index " + j);
                    indexOfSelectColumns.put(s, j);
                    if (searchTerms.containsKey(s)) {
                        indexOfSearchColumns.put(searchTerms.get(s), j);
                        // System.out.println("Search for \"" + searchTerms.get(s) + "\" at index " + j);
                    }
                }
            }
        }

        Queue<Integer> columnToReturn = new LinkedList<>();
        int count = 0;
        for (String searchTerm : indexOfSearchColumns.keySet()) {
            int columnIndex = indexOfSearchColumns.get(searchTerm);
            // System.out.println("Searching for " + searchTerm);
            for (int i = 0; i < colSize; i++) {
                String item = tableMatrix[i][columnIndex];
                if (item.equals(searchTerm)) {
                    // System.out.println("Found " + item + " at location [" + i + "," + columnIndex + "]");
                    count++;
                    if (!columnToReturn.contains(i) && count == columnsName.length) {
                        count = 0;
                        columnToReturn.add(i);
                    }
                }
            }
        }

        // Print results
        System.out.println("Results: ");
        for (int j = 0; j < rowSize; j++) {
            String item = tableMatrix[0][j];
            // Print only selected columns
            if (indexOfSelectColumns.containsKey(item)) {
                System.out.print(item + "\t\t");
            }
        }
        System.out.println();
        for (Integer i: columnToReturn) {
            for (int j = 0; j < rowSize; j++) {
                String item = tableMatrix[i][j];
                if (indexOfSelectColumns.containsValue(j)) {
                    System.out.print(item + "\t\t");
                }
            }
            System.out.println();
        }

        // Print matrix
//        for (int i = 0; i < colSize; i++) {
//            for (int j = 0; j < rowSize; j++) {
//                String item = tableMatrix[i][j];
//                System.out.print(item + "\t");
//            }
//            System.out.print("\n");
//        }
    }
}

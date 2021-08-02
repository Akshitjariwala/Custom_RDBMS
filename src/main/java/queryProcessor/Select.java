package queryProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Select {
    private static final String workingDir = System.getProperty("user.dir");

    public static void setTokens(Map<String, Object> validationTokens) {
        String databaseName;
        String tableName;
        List<String> columnsName;
        Map<String, String> searchTerms = new HashMap<>();
        System.out.println("Setting tokens...");
        boolean valid = validationTokens.containsKey("columns")
                && validationTokens.containsKey("isValid")
                && validationTokens.containsKey("where")
                && validationTokens.containsKey("tableName")
                && validationTokens.containsKey("databaseName");
        if (valid) {
            databaseName = (String) validationTokens.get("databaseName");
            tableName = (String) validationTokens.get("tableName");
            columnsName = (List<String>) validationTokens.get("columns");
            List<String> whereList = (List<String>) validationTokens.get("where");
            for (String s : whereList) {
                if (s.contains("=")) {
                    String k = s.split("=")[0];
                    String v = s.split("=")[1];
                    searchTerms.put(k, v);
                }
            }
            System.out.println("SELECT " + columnsName.toString().replace('[', '(').replace(']', ')') + " FROM " + tableName + " WHERE " + searchTerms.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));
        }
    }

    public static void execute(Map<String, Object> validationTokens) {
//        String databaseName = "database1";
//        String tableName = "user_data";
//        List<String> columnsName = Arrays.asList("user_name", "user_email", "user_contact");
//        Map<String, String> searchTerms = new HashMap<>();
//        searchTerms.put("user_name", "alex");
//        searchTerms.put("user_email", "alex.mark@gmail.com");
//        searchTerms.put("user_contact", "5566223311");
        // Sample query: select column1,column2,column3 from table_a where userid = 100 and user = akshit
        // Sample query: SELECT user_name, user_email, user_contact FROM user_data WHERE user_email=alex.mark@gmail.com, user_name=alex, user_contact=5566223311
        // tableName
        // condition(s) - columns/values
        String databaseName;
        String tableName;
        List<String> columnsName;
        Map<String, String> searchTerms = new HashMap<>();
        boolean valid = validationTokens.containsKey("columns")
                && validationTokens.containsKey("isValid")
                && validationTokens.containsKey("where")
                && validationTokens.containsKey("tableName")
                && validationTokens.containsKey("databaseName");
        if (valid) {
            databaseName = (String) validationTokens.get("databaseName");
            tableName = (String) validationTokens.get("tableName");
            columnsName = (List<String>) validationTokens.get("columns");
            List<String> whereList = (List<String>) validationTokens.get("where");
            for (String s : whereList) {
                if (s.contains("=")) {
                    String k = s.split("=")[0];
                    String v = s.split("=")[1];
                    searchTerms.put(k, v);
                }
            }
            System.out.print("QUERY: ");
            System.out.println("SELECT " + columnsName.toString().replace('[', '(').replace(']', ')') + " FROM " + tableName + " WHERE " + searchTerms.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));
            // TODO: Open table file
            List<String> rows = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    rows.add(line);
                }
            } catch (IOException e) {
                System.out.println("Invalid table");
            }
            // Get number of columns
            int colSize = rows.get(0).split("\\|\\|").length;
            int rowSize = rows.size();
            // insert table into matrix
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
                        if (!columnToReturn.contains(i) && count == columnsName.size()) {
                            count = 0;
                            columnToReturn.add(i);
                        }
                    }
                }
            }

            // Print results
            System.out.println();
            System.out.println("RESULTS: ");
            for (int j = 0; j < rowSize; j++) {
                String item = tableMatrix[0][j];
                // Print only selected columns
                if (indexOfSelectColumns.containsKey(item)) {
                    System.out.print(item + "\t\t");
                }
            }
            System.out.println();
            for (Integer i : columnToReturn) {
                for (int j = 0; j < rowSize; j++) {
                    String item = tableMatrix[i][j];
                    if (indexOfSelectColumns.containsValue(j)) {
                        System.out.print(item + "\t\t");
                    }
                }
                System.out.println();
            }
            System.out.println();

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
}

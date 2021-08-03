package queryProcessor;

import java.io.*;
import java.util.*;

public class Delete {
    private static final String workingDir = System.getProperty("user.dir");

    public static void execute(Map<String, Object> validationTokens) {
        // Sample query: delete from table_b where column1=100 and column2=akshit and column3 =100
        System.out.println(validationTokens);
        String databaseName;
        String tableName;
        List<String> columnsName;
        Map<String, String> searchTerms = new HashMap<>();
        boolean valid = validationTokens.containsKey("whereArray")
                && validationTokens.containsKey("tableName")
                && validationTokens.containsKey("databaseName");
        if (!valid) {
            System.out.println("Invalid tokens");
            return;
        }
        databaseName = (String) validationTokens.get("databaseName");
        tableName = (String) validationTokens.get("tableName");
        // columnsName = (List<String>) validationTokens.get("columns");
        List<String> whereList = (List<String>) validationTokens.get("whereArray");
        for (String s : whereList) {
            if (s.contains("=")) {
                String k = s.split("=")[0];
                String v = s.split("=")[1];
                searchTerms.put(k, v);
            }
        }
        System.out.print("QUERY: ");
        System.out.println("DELETE FROM " + tableName + " WHERE " + searchTerms.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));
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

        final String path = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";
        QueryProcessor.loadTableToArray(path);
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

        // Print table
//            for (int i = 0; i < colSize; i++) {
//                for (int j = 0; j < rowSize; j++) {
//                    String item = tableMatrix[i][j];
//                    System.out.print(item + "\t");
//                }
//                System.out.print("\n");
//            }

        // Match where columns to index number
        Map<String, Integer> indexOfSearchColumns = new HashMap<>();
        for (String s : searchTerms.keySet()) {
            for (int j = 0; j < rowSize; j++) {
                if (tableMatrix[0][j].equals(s)) {
                    indexOfSearchColumns.put(searchTerms.get(s), j);
                    // System.out.println("Search for \"" + searchTerms.get(s) + "\" at index " + j);
                }
            }
        }

        Set<String> matchedValues = new HashSet<>();
        Set<Integer> rowsToDelete = new HashSet<>();
        for (int i = 0; i < colSize; i++) {
            for (String s : indexOfSearchColumns.keySet()) {
                int j = indexOfSearchColumns.get(s);
                String item = tableMatrix[i][j];
                if (item.equals(s)) {
                    matchedValues.add(item);
                    rowsToDelete.add(i);
                }
                // System.out.print(item + "\t");
            }
            // System.out.print("\n");
        }

        System.out.println();

        if (matchedValues.size() == searchTerms.size()) {
            for (Integer i : rowsToDelete) {
                System.out.print("Delete row " + i + " (");
                for (int j = 0; j < rowSize; j++) {
                    String item = tableMatrix[i][j];
                    if (j < rowSize - 1) {
                        System.out.print(item + ", ");
                    } else {
                        System.out.print(item);
                    }
                }
                System.out.println(")");
            }
        }

        System.out.println();

        // Write to file
        try (FileWriter fw = new FileWriter(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < colSize; i++) {
                if (!rowsToDelete.contains(i)) {
                    for (int j = 0; j < rowSize; j++) {
                        String item = tableMatrix[i][j];
                        if (j < rowSize - 1) {
                            System.out.print(item + "\t||\t");
                            out.print(item + "\t||\t");
                        } else {
                            System.out.print(item);
                            out.print(item);
                        }
                    }
                    System.out.print("\n");
                    out.print("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print results
        System.out.println();
        System.out.println("RESULTS: ");


    }
}

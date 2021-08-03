package queryProcessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Select {
    private static final String workingDir = System.getProperty("user.dir");

    public static String[][] loadTableToArray(String path) throws FileNotFoundException {
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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
        // System.out.println("[" + rowSize + ", " + colSize + "]");
        // insert table into matrix
        String[][] tableMatrix = new String[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            String[] columnValues = rows.get(i).split("\\|\\|");
            for (int j = 0; j < colSize; j++) {
                tableMatrix[i][j] = columnValues[j].trim();
            }
        }
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                System.out.print("[" + i + ", " + j + "]");
            }
            System.out.print("\n");
        }
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                String item = tableMatrix[i][j];
                System.out.print(item + "\t");
            }
            System.out.print("\n");
        }
        return tableMatrix;
    }

    public static void execute(Map<String, Object> validationTokens) {
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
            final String filePath = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";
            List<String> rows = new ArrayList<>();

            String[][] tableMatrix = new String[0][0];
            int rowSize = 0;
            int colSize = 0;
            boolean loadedTable = false;
            try {
                tableMatrix = loadTableToArray(filePath);
                rowSize = tableMatrix.length;
                colSize = tableMatrix[0].length;
                loadedTable = true;
            } catch (FileNotFoundException e) {
                System.out.println("Failed to load table");
            }

            if (!loadedTable) {
                System.out.println("Failed to load table");
                return;
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
            for (String searchTerm : indexOfSearchColumns.keySet()) {
                int columnIndex = indexOfSearchColumns.get(searchTerm);
                System.out.println("Searching for " + searchTerm);
                for (int i = 0; i < colSize; i++) {
                    String item = tableMatrix[i][columnIndex];
                    if (item.equals(searchTerm)) {
                        System.out.println("Found " + item + " at location [" + i + "," + columnIndex + "]");
                        if (!columnToReturn.contains(i)) {
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

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
        for (int row = 0; row < rowSize; row++) {
            String[] columnValues = rows.get(row).split("\\|\\|");
            for (int col = 0; col < colSize; col++) {
                tableMatrix[row][col] = columnValues[col].trim();
            }
        }
        return tableMatrix;
    }

    public static void execute(Map<String, Object> validationTokens) {
        System.out.println(validationTokens);
        Map<String, String> whereConditions = new HashMap<>();
        boolean valid = validationTokens.containsKey("columns")
                && validationTokens.containsKey("isValid")
                && validationTokens.containsKey("where")
                && validationTokens.containsKey("tableName")
                && validationTokens.containsKey("databaseName");
        if (!valid) {
            System.out.println("Tokens are invalid");
            return;
        }
        final String databaseName = (String) validationTokens.get("databaseName");
        final String tableName = (String) validationTokens.get("tableName");
        final List<String> columnsName = (List<String>) validationTokens.get("columns");
        final List<String> whereList = (List<String>) validationTokens.get("where");
        for (String s : whereList) {
            if (s.contains("=")) {
                String k = s.split("=")[0].trim();
                String v = s.split("=")[1].trim();
                whereConditions.put(k, v);
            }
        }
        System.out.print("QUERY: ");
        System.out.println("SELECT " + columnsName.toString().replace('[', '(').replace(']', ')') + " FROM " + tableName + " WHERE " + whereConditions.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));
        final String filePath = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";

        String[][] tableMatrix;
        int rowSize;
        int colSize;
        tableMatrix = QueryProcessor.loadTableToArray(filePath);
        rowSize = tableMatrix.length;
        colSize = tableMatrix[0].length;

        // Create a column index
        Map<String, Integer> columnsIndex = new HashMap<>();
        List<Integer> selectedColumns = new LinkedList<>();
        for (int j = 0; j < colSize; j++) {
            String item = tableMatrix[0][j];
            columnsIndex.put(item, j);
            if (columnsName.contains(item)) {
                selectedColumns.add(j);
            }
        }

        // Find rows that match the where clause
        TreeSet<Integer> matchedRows = new TreeSet<>();
        int numberOfConditions = whereConditions.size();
        for (int row = 1; row < rowSize; row++) {
            int conditionCounter = 0;
            for (String key : whereConditions.keySet()) {
                int col = columnsIndex.get(key);
                String value = tableMatrix[row][col];
                String whereValue = whereConditions.get(key);
                if (value.equals(whereValue)) {
                    conditionCounter++;
                    if (conditionCounter == numberOfConditions) {
                        matchedRows.add(row);
                        conditionCounter = 0;
                    }
                }
            }
        }

        // Print results
        System.out.println();
        System.out.println("----------------------------------------------------------------------------");
        // Print title row
        for (Integer col : selectedColumns) {
            String item = tableMatrix[0][col];
            System.out.format("%-24s", item);
        }
        // Print selected rows
        System.out.println();
        for (Integer row : matchedRows) {
            for (Integer col : selectedColumns) {
                String item = tableMatrix[row][col];
                System.out.format("%-24s", item);
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------------------");
    }
}

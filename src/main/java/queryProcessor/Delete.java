package queryProcessor;

import java.io.*;
import java.util.*;

public class Delete {
    private static final String workingDir = System.getProperty("user.dir");
    private static final boolean debug = true;

    public static void execute(Map<String, Object> validationTokens) {
        System.out.println(validationTokens);
        boolean valid = validationTokens.containsKey("whereArray")
                && validationTokens.containsKey("tableName")
                && validationTokens.containsKey("databaseName");
        if (!valid) {
            System.out.println("Invalid tokens");
            return;
        }
        final String databaseName = (String) validationTokens.get("databaseName");
        final String tableName = (String) validationTokens.get("tableName");
        final List<String> whereList = (List<String>) validationTokens.get("whereArray");
        final Map<String, String> whereConditions = new HashMap<>();
        for (String s : whereList) {
            if (s.contains("=")) {
                String k = s.split("=")[0];
                String v = s.split("=")[1];
                whereConditions.put(k, v);
            }
        }
        final String filePath = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";

        System.out.print("QUERY: ");
        System.out.println("DELETE FROM " + tableName + " WHERE " + whereConditions.toString().replace('{', '(').replace('}', ')').replace(",", " AND"));

        final String[][] tableMatrix = QueryProcessor.loadTableToArray(filePath);

        final int rowSize = tableMatrix[0].length;
        final int colSize = tableMatrix[0].length;

        // Create a column index
        Map<String, Integer> columnsIndex = new HashMap<>();
        for (int col = 0; col < colSize; col++) {
            String item = tableMatrix[0][col];
            columnsIndex.put(item, col);
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
        for (int col = 0; col < colSize; col++) {
            String item = tableMatrix[0][col];
            System.out.format("%-24s", item);
        }
        // Print selected rows
        System.out.println();
        for (Integer row : matchedRows) {
            for (int col = 0; col < colSize; col++) {
                String item = tableMatrix[row][col];
                System.out.format("%-24s", item);
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------------------");

        for (int row = 0; row < rowSize && !matchedRows.contains(row); row++) {
            for (int col = 0; col < colSize; col++) {
                String item = tableMatrix[row][col];
                System.out.format("%-24s", item);
            }
            System.out.println();
        }

        // Write to file
        if (debug) {
            return;
        }
        try (FileWriter fw = new FileWriter(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (int row = 0; row < rowSize && !matchedRows.contains(row); row++) {
                for (int col = 0; col < colSize; col++) {
                    out.print(tableMatrix[row][col] + "\t||\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package queryProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryProcessor {
    protected static String[][] loadTableToArray(String path) {
        List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            System.out.println("Invalid table");
        }
        if (rows.isEmpty()) {
            System.out.println("Invalid table");
        }
        // Get number of columns
        int colSize = rows.get(0).split("\\|\\|").length;
        int rowSize = rows.size();
        // insert table into matrix
        String[][] tableMatrix = new String[rowSize][colSize];
        for (int row = 0; row < rowSize; row++) {
            String[] columnValues = rows.get(row).split("\\|\\|");
            for (int col = 0; col < colSize; col++) {
                tableMatrix[row][col] = columnValues[col].trim();
                // System.out.print("[" + tableMatrix[row][col] + "]");
            }
            // System.out.println();
        }
        return tableMatrix;
    }
}

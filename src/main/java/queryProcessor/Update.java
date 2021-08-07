package queryProcessor;

import Transaction.TransactionHandler;
import Transaction.TransactionLog;
import Transaction.TransactionQueue;

import java.io.*;
import java.util.*;


public class Update {
    private static final String workingDir = System.getProperty("user.dir");
    
    public static TransactionLog transactionLog = new TransactionLog();
    
    public static void execute(Map<String, Object> validationTokens) {
        System.out.println("Update module received tokens: " + validationTokens);
        String databaseName;
        String tableName;
        Map<String, String> whereConditions = new HashMap<>();
        Map<String, String> setTerms = new HashMap<>();
        boolean valid = validationTokens.containsKey("setColumns")
                && validationTokens.containsKey("whereList")
                && validationTokens.containsKey("databaseName")
                && validationTokens.containsKey("tableName");
        if (!valid) {
            System.out.println("Invalid tokens");
            return;
        }
        databaseName = (String) validationTokens.get("databaseName");
        tableName = (String) validationTokens.get("tableName");
        List<String> whereList = (List<String>) validationTokens.get("whereList");
        for (String s : whereList) {
            if (s.contains("=")) {
                String k = s.split("=")[0];
                String v = s.split("=")[1];
                whereConditions.put(k.trim(), v.trim());
            }
        }
        List<String> setList = (List<String>) validationTokens.get("setColumns");
        for (String s : setList) {
            if (s.contains("=")) {
                String k = s.split("=")[0];
                String v = s.split("=")[1];
                setTerms.put(k.trim(), v.trim());
            }
        }
        final String rebuiltQuery = "UPDATE " + tableName + " SET " + setList.toString().replace('[', '(').replace(']', ')') + " WHERE " + whereConditions.toString().replace('{', '(').replace('}', ')').replace(",", " AND");
        final String filePath = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";
        
        List<String> queueList;
    
        System.out.println("Table is not locked.");
        int q = 0;
        try {
            /********Table is locked*********/
            System.out.println("Updating data into database.");
            /* Add update query logic here */

            System.out.println("QUERY: " + rebuiltQuery);

            final String[][] tableMatrix = QueryProcessor.loadTableToArray(filePath);
            final String[][] updatedTableMatrix = QueryProcessor.loadTableToArray(filePath);

            final int rowSize = tableMatrix.length;
            final int colSize = tableMatrix[0].length;

            // Create a column index
            Map<String, Integer> columnsIndex = new HashMap<>();
            List<Integer> selectedColumns = new LinkedList<>();
            for (int j = 0; j < colSize; j++) {
                String item = tableMatrix[0][j];
                columnsIndex.put(item, j);
                if (setTerms.containsKey(item)) {
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

            // Update table
            for (Integer row : matchedRows) {
                for (Integer col : selectedColumns) {
                    String colName = tableMatrix[0][col];
                    updatedTableMatrix[row][col] = setTerms.get(colName);
//                String item = updatedTableMatrix[row][col];
//                System.out.format("%-24s", item);
                }
                System.out.println();
            }

            // Print results
            System.out.println();
            System.out.println("\t\t\t\t\t\t\tORIGINAL TABLE\t\t\t\t\t");
            System.out.println("----------------------------------------------------------------------------");
            // Print selected rows
            for (int row = 0; row < rowSize; row++) {
                for (int col = 0; col < colSize; col++) {
                    String item = tableMatrix[row][col];
                    System.out.format("%-24s", item);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("\t\t\t\t\t\t\tUPDATED TABLE\t\t\t\t\t");
            System.out.println("----------------------------------------------------------------------------");

            for (int row = 0; row < rowSize; row++) {
                for (int col = 0; col < colSize; col++) {
                    String item = updatedTableMatrix[row][col];
                    System.out.format("%-24s", item);
                }
                System.out.println();
            }

            // Write to file
            try (FileWriter fw = new FileWriter(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt", false);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                for (int row = 0; row < rowSize; row++) {
                    for (int col = 0; col < colSize; col++) {
                        if(col == colSize-1) {
                            out.print(updatedTableMatrix[row][col]);
                        } else {
                            out.print(updatedTableMatrix[row][col] + "\t||\t");
                        }
                    }
                    out.println();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> oldValues = new ArrayList<>();
            List<String> columnList = new ArrayList<>();
            List<String> newValues = new ArrayList<>();
            for (Integer i : matchedRows) {
                for (int j = 0; j < colSize; j++) {
                    String item = tableMatrix[i][j];
                    for (String s : setTerms.keySet()) {
                        if (columnsIndex.get(s) == j) {
                            oldValues.add(item);
                            System.out.println("Set " + item + " to " + setTerms.get(s) + " at [" + i + "," + j + "]");
                            columnList.add(tableMatrix[0][j]);
                            tableMatrix[i][j] = setTerms.get(s);
                            newValues.add(setTerms.get(s));
                        }
                    }
                }
            }

            transactionLog.createTransactionLog(databaseName, tableName, columnList, oldValues, newValues,"UPDATE");

            //send oldValues , newValues, columns, databaseName, tableName
            /********Removing Query from queue*******************/
        } finally {
            System.out.println("Time Now: " + new Date());
            /********Table is unlocked*********/
        }
        System.out.println("Queue is empty now.\n");
    }
}
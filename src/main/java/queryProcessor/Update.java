package queryProcessor;

import Transaction.TransactionHandler;
import Transaction.TransactionQueue;
import java.io.*;
import java.util.*;


public class Update {
    private static final String workingDir = System.getProperty("user.dir");

    public static TransactionHandler transactionHandler;
    public static TransactionQueue transactionQueue = new TransactionQueue();
    
    public static void main(String[] args) throws IOException {
        String sqlQuery = "update employees set department_ID = 1200 where employee_Id = 1000 AND username = akshitjariwala";
        //String sqlQuery = "update employees set department_ID = 900 where employee_Id = 1000 AND username = jariwala";
        sqlQuery = sqlQuery.trim().replaceAll("\\s{2,}"," ");
        String[] queryTokens = sqlQuery.split(" ");
        String dataName = "database1";
        String tableName = "user_data.txt";
        performUpdate(dataName,tableName ,sqlQuery,queryTokens);
    }
    
    public static void performUpdate(String databaseName, String tableName,String sqlQuery,String[] queryTokens) throws IOException {
        
        transactionHandler = new TransactionHandler();
        String tablePath = databaseName+"/"+tableName;
        List<String> queueList;
        
        if(transactionHandler.checkLock(tablePath)) {
            transactionQueue.AddToQueue(sqlQuery);
            System.out.println("Your Query : "+sqlQuery);
            System.out.println("Waiting for other transactions to complete. Your query will be executed.");
        } else {
            System.out.println("Table is not locked.");
            transactionQueue.AddToQueue(sqlQuery);
            int i=0;
            queueList = transactionQueue.fetchFromQueue();
            while(i < queueList.size()){
                try {
                    transactionHandler.lockTable(tablePath);
                    /********Table is locked*********/
                    System.out.println("Updating data into database.");
                    /* Add update query logic here */
                    System.out.println("Executing Query : " + queueList.get(i));
                    /********Removing Query from queue*******************/
                    transactionQueue.removeFromQueue(queueList.get(i));
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                } finally {
                    transactionHandler.unlockTable(tablePath, tableName);
                    System.out.println("Time Now: " + new Date());
                    /********Table is unlocked*********/
                }
                System.out.println("Queue is empty now.\n");
                queueList = transactionQueue.fetchFromQueue();
            }
        }
    }

    public static void execute(Map<String, Object> validationTokens) {
        System.out.println(validationTokens);
        String databaseName;
        String tableName;
        List<String> columnsName;
        Map<String, String> searchTerms = new HashMap<>();
        boolean valid = validationTokens.containsKey("isValid")
                && validationTokens.containsKey("whereArray")
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
            for (int i = 0; i < colSize; i++) {
                for (int j = 0; j < rowSize; j++) {
                    String item = tableMatrix[i][j];
                    System.out.print(item + "\t");
                }
                System.out.print("\n");
            }

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
    }
}
package queryProcessor;

import Transaction.TransactionHandler;
import Transaction.TransactionLog;
import Transaction.TransactionQueue;

import java.io.*;
import java.util.*;


public class Update {
    private static final String workingDir = System.getProperty("user.dir");

    public static TransactionHandler transactionHandler;
    public static TransactionQueue transactionQueue;
    public static TransactionLog transactionLog = new TransactionLog();

    public static void main(String[] args) throws IOException {
        String sqlQuery = "update employees set department_ID = 1200 where employee_Id = 1000 AND username = akshitjariwala";
        //String sqlQuery = "update employees set department_ID = 900 where employee_Id = 1000 AND username = jariwala";
        sqlQuery = sqlQuery.trim().replaceAll("\\s{2,}", " ");
        String[] queryTokens = sqlQuery.split(" ");
        String dataName = "database1";
        String tableName = "user_data.txt";
        //performUpdate(dataName,tableName ,sqlQuery,queryTokens);
    }

    public static void performUpdate(String databaseName, String tableName, String sqlQuery, String[] queryTokens) throws IOException {

        transactionQueue = new TransactionQueue(databaseName);
        transactionHandler = new TransactionHandler(databaseName);
        String tablePath = databaseName + "/" + tableName;
        List<String> queueList;

        if (transactionHandler.checkLock(tablePath)) {
            transactionQueue.AddToQueue(sqlQuery);
            System.out.println("Your Query : " + sqlQuery);
            System.out.println("Waiting for other transactions to complete. Your query will be executed.");
        } else {
            System.out.println("Table is not locked.");
            transactionQueue.AddToQueue(sqlQuery);
            int q = 0;
            queueList = transactionQueue.fetchFromQueue();
            while (q < queueList.size()) {
                try {
                    transactionHandler.lockTable(tablePath);
                    /********Table is locked*********/
                    System.out.println("Updating data into database.");
                    /* Add update query logic here */
                    System.out.println("Executing Query : " + queueList.get(q));
                    /********Removing Query from queue*******************/
                    transactionQueue.removeFromQueue(queueList.get(q));
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

    public static void executeNew(Map<String, Object> validationTokens) {
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

        transactionQueue = new TransactionQueue(databaseName);
        transactionHandler = new TransactionHandler(databaseName);
        List<String> queueList;

        try {
            if (transactionHandler.checkLock(filePath)) {
                transactionQueue.AddToQueue(rebuiltQuery);
                System.out.println("Your Query : " + rebuiltQuery);
                System.out.println("Waiting for other transactions to complete. Your query will be executed.");
            } else {
                System.out.println("Table is not locked.");
                transactionQueue.AddToQueue(rebuiltQuery);
                int q = 0;
                queueList = transactionQueue.fetchFromQueue();
                while (q < queueList.size()) {
                    try {
                        transactionHandler.lockTable(filePath);
                        /********Table is locked*********/
                        System.out.println("Updating data into database.");
                        /* Add update query logic here */

                        System.out.println("QUERY: " + rebuiltQuery);

                        final String[][] tableMatrix = QueryProcessor.loadTableToArray(filePath);
                        final String[][] updatedTableMatrix = QueryProcessor.loadTableToArray(filePath);

                        final int rowSize = tableMatrix[0].length;
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
                        try (FileWriter fw = new FileWriter(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".temp", false);
                             BufferedWriter bw = new BufferedWriter(fw);
                             PrintWriter out = new PrintWriter(bw)) {
                            for (int row = 0; row < rowSize; row++) {
                                for (int col = 0; col < colSize; col++) {
                                    out.print(updatedTableMatrix[row][col] + "\t||\t");
                                }
                                out.println();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // transactionLog.createTransactionLog(databaseName, tableName, columnList, oldValues, newValues);

                        //send oldValues , newValues, columns, databaseName, tableName
                        /********Removing Query from queue*******************/
                        transactionQueue.removeFromQueue(queueList.get(q));
                        Thread.sleep(70000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    } finally {
                        transactionHandler.unlockTable(filePath, tableName);
                        System.out.println("Time Now: " + new Date());
                        /********Table is unlocked*********/
                    }
                    System.out.println("Queue is empty now.\n");
                    queueList = transactionQueue.fetchFromQueue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        transactionQueue = new TransactionQueue(databaseName);
        transactionHandler = new TransactionHandler(databaseName);
        List<String> queueList;

        try {
            if (transactionHandler.checkLock(filePath)) {
                transactionQueue.AddToQueue(rebuiltQuery);
                System.out.println("Your Query : " + rebuiltQuery);
                System.out.println("Waiting for other transactions to complete. Your query will be executed.");
            } else {
                System.out.println("Table is not locked.");
                transactionQueue.AddToQueue(rebuiltQuery);
                int q = 0;
                queueList = transactionQueue.fetchFromQueue();
                while (q < queueList.size()) {
                    try {
                        transactionHandler.lockTable(filePath);
                        /********Table is locked*********/
                        System.out.println("Updating data into database.");
                        /* Add update query logic here */

                        System.out.println("QUERY: " + rebuiltQuery);

                        final String[][] tableMatrix = QueryProcessor.loadTableToArray(filePath);
                        final String[][] updatedTableMatrix = QueryProcessor.loadTableToArray(filePath);

                        final int rowSize = tableMatrix[0].length;
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
                        try (FileWriter fw = new FileWriter(workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".temp", false);
                             BufferedWriter bw = new BufferedWriter(fw);
                             PrintWriter out = new PrintWriter(bw)) {
                            for (int row = 0; row < rowSize; row++) {
                                for (int col = 0; col < colSize; col++) {
                                    out.print(updatedTableMatrix[row][col] + "\t||\t");
                                }
                                out.println();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // transactionLog.createTransactionLog(databaseName, tableName, columnList, oldValues, newValues);

                        //send oldValues , newValues, columns, databaseName, tableName
                        /********Removing Query from queue*******************/
                        transactionQueue.removeFromQueue(queueList.get(q));
                        Thread.sleep(70000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    } finally {
                        transactionHandler.unlockTable(filePath, tableName);
                        System.out.println("Time Now: " + new Date());
                        /********Table is unlocked*********/
                    }
                    System.out.println("Queue is empty now.\n");
                    queueList = transactionQueue.fetchFromQueue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeOld(Map<String, Object> validationTokens) {
        System.out.println("Update module received tokens: " + validationTokens);

        String databaseName;
        String tableName;
        Map<String, String> whereTerms = new HashMap<>();
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
                whereTerms.put(k.trim(), v.trim());
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

        final String rebuiltQuery = "UPDATE " + tableName + " SET " + setList.toString().replace('[', '(').replace(']', ')') + " WHERE " + whereTerms.toString().replace('{', '(').replace('}', ')').replace(",", " AND");
        System.out.println("QUERY: " + rebuiltQuery);
        final String tablePath = workingDir + "/appdata/database/" + databaseName + "/" + tableName + ".txt";
        final String tablePathForLock = databaseName + "/" + tableName + ".txt";

        transactionQueue = new TransactionQueue(databaseName);
        transactionHandler = new TransactionHandler(databaseName);
        List<String> queueList;

        try {
            if (transactionHandler.checkLock(tablePathForLock)) {
                transactionQueue.AddToQueue(rebuiltQuery);
                System.out.println("Your Query : " + rebuiltQuery);
                System.out.println("Waiting for other transactions to complete. Your query will be executed.");
            } else {
                System.out.println("Table is not locked.");
                transactionQueue.AddToQueue(rebuiltQuery);
                int q = 0;
                queueList = transactionQueue.fetchFromQueue();
                while (q < queueList.size()) {
                    try {
                        transactionHandler.lockTable(tablePathForLock);
                        /********Table is locked*********/
                        System.out.println("Updating data into database.");
                        /* Add update query logic here */
                        System.out.println("Executing Query : " + queueList.get(q));
                        // UPDATE user_data SET user_name=newalex, user_contact=new5566223311 WHERE user_name=alex AND user_contact=5566223311
                        // {setColumns=[user_name=newalex, user_contact=new5566223311], whereColumnList=[user_contact, 5566223311], setColumnList=[user_contact, new5566223311], databaseName=database1, isValid=true, whereList=[user_name=alex, user_contact=5566223311], tableName=user_data}
                        List<String> rows = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader(tablePath))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                rows.add(line.trim());
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

                        // Create an index for the string
                        Map<String, Integer> columnsIndex = new HashMap<>();
                        for (int j = 0; j < colSize; j++) {
                            String item = tableMatrix[0][j];
                            columnsIndex.put(item, j);
                        }

                        List<String> oldValues = new ArrayList<>();
                        List<String> columnList = new ArrayList<>();

                        // Match where columns to index number
                        Map<String, Integer> indexOfSearchColumns = new HashMap<>();
                        for (String s : whereTerms.keySet()) {
                            for (int j = 0; j < colSize; j++) {
                                if (tableMatrix[0][j].equals(s)) {
                                    indexOfSearchColumns.put(whereTerms.get(s), j);
                                    System.out.println("Search for \"" + whereTerms.get(s) + "\" at index " + j);
                                    //columnList.add(s);
                                    //oldValues.add(whereTerms.get(s));
                                }
                            }
                        }

                        Set<String> matchedValues = new HashSet<>();
                        Set<Integer> rowsToUpdate = new HashSet<>();
                        for (int i = 0; i < rowSize; i++) {
                            for (String s : indexOfSearchColumns.keySet()) {
                                int j = indexOfSearchColumns.get(s);
                                String item = tableMatrix[i][j];
                                if (item.equals(s)) {
                                    System.out.println("Found " + s + " at [" + i + "," + j + "]");
                                    matchedValues.add(item);
                                    rowsToUpdate.add(i);
                                }
                                // System.out.print(item + "\t");
                            }
                            // System.out.print("\n");
                        }

                        List<String> newValues = new ArrayList<>();
                        boolean writeToFile = false;
                        System.out.println("Matched " + matchedValues.size() + " of " + whereTerms.size() + " values");
                        if (matchedValues.size() == whereTerms.size()) {
                            for (Integer i : rowsToUpdate) {
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
                            writeToFile = true;
                        } else {
                            System.out.println("No update performed");
                        }

                        transactionLog.createTransactionLog(databaseName, tableName, columnList, oldValues, newValues);

                        //send oldValues , newValues, columns, databaseName, tableName

                        // Write to file
                        if (writeToFile) {

                        }
                        try (FileWriter fw = new FileWriter(tablePath, false);
                             BufferedWriter bw = new BufferedWriter(fw);
                             PrintWriter out = new PrintWriter(bw)) {
                            for (int i = 0; i < rowSize; i++) {
                                for (int j = 0; j < colSize; j++) {
                                    String item = tableMatrix[i][j];
                                    if (j < colSize - 1) {
                                        System.out.print(item + "\t||\t");
                                        if (writeToFile) {
                                            out.print(item + "\t||\t");
                                        }
                                    } else {
                                        System.out.print(item);
                                        if (writeToFile) {
                                            out.print(item);
                                        }
                                    }
                                }
                                System.out.print("\n");
                                out.print("\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /********Removing Query from queue*******************/
                        transactionQueue.removeFromQueue(queueList.get(q));
                        Thread.sleep(70000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    } finally {
                        transactionHandler.unlockTable(tablePathForLock, tableName);
                        System.out.println("Time Now: " + new Date());
                        /********Table is unlocked*********/
                    }
                    System.out.println("Queue is empty now.\n");
                    queueList = transactionQueue.fetchFromQueue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
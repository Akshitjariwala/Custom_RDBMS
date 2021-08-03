package queryProcessor;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Map;

public class Drop {

    //drop the table
    public void dropTable(Map<String, Object> validationTokens) throws IOException {
        for (Map.Entry<String, Object> tokens1: validationTokens.entrySet()) {
            System.out.println("keys: "  + tokens1.getKey() + "\n" + "value: " + tokens1.getValue());
        }
        String tableName = validationTokens.get("tableToDrop").toString();
        ddTableDrop(tableName);
        String databasePath = "appdata/database/database1/";
        String tableFile = tableName + ".txt";
        File file  = new File(databasePath+ tableFile);
        boolean result = Files.deleteIfExists(file.toPath());
        if(result)
            System.out.println("table: "+ tableName  +" dropped successfully");
        else
            System.out.println("Cannot drop the table as it is locked by another process");
    }

    //drop the entry from the data dictionary as well
    public void ddTableDrop(String tableName) throws IOException {
        String databasePath = "appdata/database/database1/";
        String ddFile = "data_dictionary.txt";
        File file  = new File(databasePath + ddFile);
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));
        Files.lines(file.toPath())
                .filter(row -> !row.contains(tableName))
                .forEach(printWriter::println);
        printWriter.flush();
        printWriter.close();
        file.delete();
        tempFile.renameTo(file);
    }
}

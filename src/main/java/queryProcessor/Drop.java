package queryProcessor;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Map;

public class Drop {
    public void dropTable(Map<String, Object> validationTokens) throws IOException {
    }

    public void ddTableDrop(String tableName) throws IOException {
        String databasePath = "appdata/database/database1/";
        String ddFile = "data_dictionary.txt";
        //String tempFile = "temp";
        //System.out.println("debug3");
        File file  = new File(databasePath + ddFile);
        File temp = new File(file.getAbsolutePath() + ".tmp");
        PrintWriter out = new PrintWriter(new FileWriter(temp));
        Files.lines(file.toPath())
                .filter(line -> !line.contains(tableName))
                .forEach(out::println);
        out.flush();
        out.close();
        file.delete();
        temp.renameTo(file);
    }
}

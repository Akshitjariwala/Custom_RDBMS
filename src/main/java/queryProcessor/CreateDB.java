package queryProcessor;
import java.io.File;

public class CreateDB {
    private static final String workingDir = System.getProperty("user.dir");

    public static void create(String databaseName) {
        // Create a folder
        boolean databaseDir = new File(workingDir + "/appdata/database/" + databaseName).mkdirs();
        // Create a data dictionary
    }
}

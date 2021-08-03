package queryProcessor;
import logger.Logger;

import java.io.File;

public class CreateDB {
    private static final String workingDir = System.getProperty("user.dir");

    public static boolean execute(String databaseName) {
        // Create a folder
        boolean databaseDir = new File(workingDir + "/appdata/database/" + databaseName).mkdirs();
        // log action
        Logger.createLog(databaseName);
        Logger.log("Created database: " + databaseName);
        return databaseDir;
    }
}

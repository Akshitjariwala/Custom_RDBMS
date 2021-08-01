package queryProcessor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreateDB {
    private static final String workingDir = System.getProperty("user.dir");

    public CreateDB() {
        Map<String, String> tokens = new HashMap<>();
        // Create a direct
        File theDir = new File("/path/directory");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }
}

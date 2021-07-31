package queryProcessor;

import java.io.*;
import java.nio.channels.OverlappingFileLockException;


public class Update {
    
    public static void main(String[] args) {
        performUpdate("database1", "QueryLogs.txt");
    }
    
    public static void performUpdate(String databaseName, String tableName) {
    
        //String filePath = databasePath+databaseName+"/"+tableName;
        
        String tablePath = databaseName+"/"+tableName;
        
    }
}
package queryProcessor;

public class QueryProcessor {
    public String processQuery(String query) {
        String queryType = null;
        boolean queryIsSelect = false;
        boolean queryIsUpdate = false;
        boolean queryIsInsert = false;
        boolean queryIsTableUpdate = false;
        boolean queryIsDatabaseCreate = false;
        if (queryIsSelect) {
            queryType = "SELECT";
        }
        if (queryIsUpdate) {
            queryType = "UPDATE";
        }
        if (queryIsInsert) {
            queryType = "INSERT";
        }
        if (queryIsTableUpdate) {
            queryType = "TABLE CREATE";
        }
        if (queryIsDatabaseCreate) {
            queryType = "DATABASE CREATE";
        }
        return queryType;
    }

    public void createDatabase() {
        // Create a new directory in appdata/database/
        // Create a new file to store db info
    }

    public void createTable() {
        // Check that database exists
    }
}

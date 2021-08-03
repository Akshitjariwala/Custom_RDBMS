package queryProcessor;

import logger.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateDBTest {

    @Test
    void execute() {
        CreateDB.execute("CreateDBTest");
    }
}
package logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void createLog() {
        Logger logger = new Logger();
        logger.createLog();
    }

    @Test
    void log() {
        Logger logger = new Logger();
        logger.log("Hello World");
    }

    @Test
    void printLog() {
        Logger logger = new Logger();
        logger.printLog();
    }
}
package logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void createLog() {
        Logger.createLog("loggerTestDB");
    }

    @Test
    void log() {
        Logger.createLog("loggerTestDB");
        Logger.log("Hello World");
    }

    @Test
    void printLog() {
        Logger logger = new Logger();
        Logger.createLog("loggerTestDB");
        Logger.printLog();
    }
}
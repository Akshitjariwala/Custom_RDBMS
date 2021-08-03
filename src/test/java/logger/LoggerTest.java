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
        Logger.createLog("database1");
        Logger.log("Hello World");
        Logger.log("This is an event");
        Logger.log("Something happened");
    }

    @Test
    void printLog() {
        Logger.createLog("loggerTestDB");
    }
}
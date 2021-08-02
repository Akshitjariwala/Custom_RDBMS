package logger;

import java.io.*;

public class Logger {
    private static String logPath;

    public static boolean createLog(String database) {
        logPath = System.getProperty("user.dir") + "/appdata/database/" + database + "/log.txt";
        try {
            File log = new File(logPath);
            return log.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

    public static void log(String event) {
        try(FileWriter fw = new FileWriter(logPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            String date = java.time.LocalDateTime.now().toString();
            out.println(date + " " + event);
        } catch (IOException ignored) {
        }
    }

    public static void printLog() {
        try (BufferedReader br = new BufferedReader(new FileReader(logPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

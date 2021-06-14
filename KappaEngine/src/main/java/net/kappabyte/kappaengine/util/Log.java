package net.kappabyte.kappaengine.util;

import java.util.Calendar;

public class Log {

    public static final class LogColor {
        public static final String RESET = "\u001B[0m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";

        public static final String BK_BLACK = "\u001B[40m";
        public static final String BK_RED = "\u001B[41m";
        public static final String BK_GREEN = "\u001B[42m";
        public static final String BK_YELLOW = "\u001B[43m";
        public static final String BK_BLUE = "\u001B[44m";
        public static final String BK_PURPLE = "\u001B[45m";
        public static final String BK_CYAN = "\u001B[46m";
        public static final String BK_WHITE = "\u001B[47m";
            }

    public enum LogLevel {
        DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);

        private int level;
        private LogLevel(int level) {
            this.level = level;
        }

        public static boolean display(LogLevel level, LogLevel current) {
            return current.level >= level.level;
        }
    }

    private static LogLevel level = LogLevel.DEBUG;

    public static void setLogLevel(LogLevel level) {
        Log.level = level;

        debug("Updated Log Level to: " + level.toString());
    }

    public static void debug(String message) {
        if(LogLevel.display(level, LogLevel.DEBUG)) {
            String time = Calendar.getInstance().getTime().toString();
            System.out.println(LogColor.CYAN + "[DEBUG] (" + time + "): " + message + LogColor.RESET);
        }
    }

    public static void info(String message) {
        if(LogLevel.display(level, LogLevel.INFO)) {
            String time = Calendar.getInstance().getTime().toString();
            System.out.println(LogColor.GREEN + "[INFO] (" + time + "): " + message + LogColor.RESET);
        }
    }

    public static void warn(String message) {
        if(LogLevel.display(level, LogLevel.WARN)) {
            String time = Calendar.getInstance().getTime().toString();
            System.out.println(LogColor.YELLOW + "[WARN] (" + time + "): " + message + LogColor.RESET);
        }
    }

    public static void error(String message) {
        if(LogLevel.display(level, LogLevel.ERROR)) {
            String time = Calendar.getInstance().getTime().toString();
            System.out.println(LogColor.RED + "[ERROR] (" + time + "): " + message + LogColor.RESET);
        }
    }

    public static void fatal(String message) {
        if(LogLevel.display(level, LogLevel.FATAL)) {
            String time = Calendar.getInstance().getTime().toString();
            System.out.println(LogColor.WHITE + LogColor.BK_RED + "[FATAL] (" + time + "): " + message + LogColor.RESET);
        }
    }

    public static void stack() {
        new Exception().printStackTrace();
    }
}

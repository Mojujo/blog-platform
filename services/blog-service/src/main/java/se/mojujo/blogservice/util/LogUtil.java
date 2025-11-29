package se.mojujo.blogservice.util;

import org.slf4j.Logger;

public final class LogUtil {

    private LogUtil() {} // prevent instantiation

    public static void info(Logger logger, String event, String message, Object... keyValues) {
        log(logger, "INFO", event, message, keyValues);
    }

    public static void warn(Logger logger, String event, String message, Object... keyValues) {
        log(logger, "WARN", event, message, keyValues);
    }

    public static void error(Logger logger, String event, String message, Object... keyValues) {
        log(logger, "ERROR", event, message, keyValues);
    }

    private static void log(Logger logger, String level, String event, String message, Object... keyValues) {
        StringBuilder sb = new StringBuilder();
        sb.append("event=").append(event);

        if (keyValues != null && keyValues.length % 2 == 0) {
            for (int i = 0; i < keyValues.length; i += 2) {
                sb.append(" ").append(keyValues[i]).append("=").append(keyValues[i + 1]);
            }
        }

        if (message != null && !message.isBlank()) {
            sb.append(" message=\"").append(message).append("\"");
        }

        switch (level) {
            case "INFO" -> logger.info(sb.toString());
            case "WARN" -> logger.warn(sb.toString());
            case "ERROR" -> logger.error(sb.toString());
        }
    }

    public static String maskToken(String token) {
        if (token == null || token.length() < 10) return "****";
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
}

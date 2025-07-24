package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for logging.
 *
 * @param logToFile Whether to log to a file.
 * @param logFilePath Path to the log file.
 */
public record LoggingConfig(boolean logToFile, String logFilePath) {}
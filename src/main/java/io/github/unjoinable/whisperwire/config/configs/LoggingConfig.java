package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for logging.
 *
 * @param logToFile Whether to log to a file.
 * @param logFilePath Path to the log file.
 */
public record LoggingConfig(
        boolean logToFile,
        String logFilePath) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean logToFile;
        private String logFilePath;

        public Builder logToFile(boolean logToFile) {
            this.logToFile = logToFile;
            return this;
        }

        public Builder logFilePath(String logFilePath) {
            this.logFilePath = logFilePath;
            return this;
        }

        public LoggingConfig build() {
            return new LoggingConfig(logToFile, logFilePath);
        }
    }
}
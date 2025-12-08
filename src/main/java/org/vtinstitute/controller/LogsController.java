package org.vtinstitute.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogsController {
    private static final Logger logger = LogManager.getLogger(LogsController.class);

    public void logInfo(String text) {
        logger.info(text);
    }

    public void logError(String text) {
        logger.error(text);
    }

    public void logWarn(String text) {
        logger.warn(text);
    }
}

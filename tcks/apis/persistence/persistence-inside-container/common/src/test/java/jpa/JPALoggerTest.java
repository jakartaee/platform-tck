/*
 * Copyright (c) "2025" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */
package jpa;

import ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogger;
import ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Validate the TSLogger class and the client side LogFileProcessor.
 */
public class JPALoggerTest {
    @BeforeEach
    public void setup() {
        System.out.println("JPALoggerTest.setup");
        TSLogger.clearLogger();
        System.clearProperty("log.file.location");
        System.clearProperty(TSLogger.LOG_NAME_DEFAULT_PROPERTY);
    }

    /**
     * Validate that if the log.file.location is set, the log file is created in that location
     * and that the LogFileProcessor uses that location.
     */
    @Test
    public void testLogFileLocation() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        System.out.println("tmpDir: "+tmpDir);
        System.setProperty("log.file.location", tmpDir);

        TSLogger logger = TSLogger.getInstance();
        Assertions.assertNotNull(logger);
        logger.log("testLogFileLocation");

        File logFile = new File(tmpDir + File.separator + TSLogger.LOG_NAME);
        System.out.println("Checking: "+logFile.getAbsolutePath());
        Assertions.assertTrue(logFile.exists());

        LogFileProcessor processor = new LogFileProcessor(System.getProperties());
        Assertions.assertTrue(processor.fetchLog());
    }

    /**
     * Validate that if the log.file.location is set to a non-tmp directory, it is created there
     * and that the LogFileProcessor uses that location.
     */
    @Test
    public void testNonTmpLogFileLocation() {
        File logDir = new File("target");
        System.out.println("logDir: "+logDir.getAbsolutePath());
        System.setProperty("log.file.location", logDir.getAbsolutePath() + File.separator + TSLogger.LOG_NAME);

        TSLogger logger = TSLogger.getInstance();
        Assertions.assertNotNull(logger);
        File logFile = new File(logDir + File.separator + TSLogger.LOG_NAME);
        Assertions.assertTrue(logFile.exists());
        logger.log("testNonTmpLogFileLocation");

        LogFileProcessor processor = new LogFileProcessor(System.getProperties());
        Assertions.assertTrue(processor.fetchLog());
    }

    /**
     * Validate that if the log.file.location is not set, the log file is created in the default location
     * and that the LogFileProcessor uses that location.
     */
    @Test
    public void testDefaultedLogFileLocation() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        System.out.println("tmpDir: "+tmpDir);
        // Not setting log.file.location

        TSLogger logger = TSLogger.getInstance();
        Assertions.assertNotNull(logger);
        File logFile = new File(logger.getLogFile());
        Assertions.assertTrue(logFile.exists());
        logger.log("testLogFileLocation");

        LogFileProcessor processor = new LogFileProcessor(System.getProperties());
        Assertions.assertTrue(processor.fetchLog());
    }

}

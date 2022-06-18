/*
 * Copyright \(c\) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the Apache Software License 2.0 which is available at:
 *  https://www.apache.org/licenses/LICENSE-2.0.
 *
 *  SPDX-License-Identifier: Apache-2.0
 */
package ee.jakarta.tck.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;

/**
 * An ISuiteListener that logs the tests that are excluded from a suite run as well as the tests
 * that are being executed as part of the suite with any group(s) if the test has them.
 */
public class SuiteLogger implements ISuiteListener {
    private static final Logger logger = Logger.getLogger(SuiteLogger.class.getName());
    private static final String CONFIGURATION_FILE_PATH = "target/cdi-lite-tck-info.log";

    @Override
    public void onStart(ISuite suite) {
        try {
            FileHandler fh = new FileHandler(CONFIGURATION_FILE_PATH);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.info("Logging suite tests to file: "+CONFIGURATION_FILE_PATH);
        } catch (Exception e) {
            logger.warning("Failed to create file handler for path: "+CONFIGURATION_FILE_PATH);
            e.printStackTrace();
        }

        // Log the excluded methods
        Collection<ITestNGMethod> exluded = suite.getExcludedMethods();
        StringBuilder msg = new StringBuilder();
        for(ITestNGMethod method : exluded) {
            msg.append(method.getTestClass().getName());
            msg.append('#');
            msg.append(method.getMethodName());
            msg.append('\n');
        }
        logger.log(Level.INFO, "+++ Excluded Methods({0}): {1}", new Object[]{exluded.size(), msg});

        // Log the methods that are to be run
        List<ITestNGMethod> allMethods = suite.getAllMethods();
        msg.setLength(0);
        for(ITestNGMethod method : allMethods) {
            msg.append(method.getTestClass().getName());
            msg.append('#');
            msg.append(method.getMethodName());
            String[] groups = method.getGroups();
            if(groups != null && groups.length > 0) {
                msg.append(";groups=");
                msg.append(Arrays.asList(groups));
            }
            msg.append('\n');
        }
        logger.log(Level.INFO, "+++ All Methods({0}): {1}", new Object[]{allMethods.size(), msg});
    }
}

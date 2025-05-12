/*
 * Copyright (c) 2021-2025 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.glassfish.persistence.tck;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.LoggingListener;

public class MyTestExecutionListener implements TestExecutionListener {

    private final LoggingListener loggingListener = LoggingListener.forJavaUtilLogging(Level.INFO);
    private final TestExecutionListener[] delegates = new TestExecutionListener[] {loggingListener};

    long testCount;
    AtomicInteger currentTest = new AtomicInteger();

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        LogManager.getLogManager().getLogger(LoggingListener.class.getName()).setLevel(Level.INFO);

        Stream.of(delegates).forEach( l -> l.testPlanExecutionStarted(testPlan));

        testCount = testPlan.countTestIdentifiers(
                testIdentifier -> testIdentifier.isTest()
            );

        Logger logger = Logger.getLogger(LoggingListener.class.getName());

        logger.info("******************** Total number of tests: " + testCount);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        Stream.of(delegates).forEach( l -> l.reportingEntryPublished(testIdentifier, entry));
        Logger logger = Logger.getLogger(LoggingListener.class.getName());
        logger.info(entry.getKeyValuePairs().get("value"));
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        Logger logger = Logger.getLogger(LoggingListener.class.getName());

        String fullMethodName = "";

        if (testIdentifier.getSource().isPresent()) {
            if (testIdentifier.getSource().get() instanceof MethodSource methodSource) {
                fullMethodName = methodSource.getClassName() + "#" + methodSource.getMethodName();

                logger.info(
                    "\n******************** Running test \u001B[1m" + fullMethodName + "\u001B[0m \033[1;32m (" +
                    currentTest.incrementAndGet()  + "/" + testCount + ")\u001B[0m \n");

            } else if (testIdentifier.getSource().get() instanceof ClassSource classSource) {
                logger.info(
                    "\n\n=========== Switching to test class \u001B[1m" + classSource.getClassName() + "\u001B[0m \n");
            }
        }

        Stream.of(delegates).forEach( l -> l.executionStarted(testIdentifier));
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        Stream.of(delegates).forEach( l -> l.executionFinished(testIdentifier, testExecutionResult));
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        Stream.of(delegates).forEach( l -> l.executionSkipped(testIdentifier, reason));
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        Stream.of(delegates).forEach( l -> l.dynamicTestRegistered(testIdentifier));
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        Stream.of(delegates).forEach( l -> l.testPlanExecutionFinished(testPlan));
    }



}

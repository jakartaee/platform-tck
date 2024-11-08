/*
 * Copyright 2024 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package tck.arquillian.protocol.appclient;

import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.container.spi.context.annotation.DeploymentScoped;
import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestMethodExecutor;
import org.jboss.arquillian.test.spi.TestResult;
import tck.arquillian.protocol.common.TsTestPropsBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class AppClientMethodExecutor implements ContainerMethodExecutor {
    static Logger log = Logger.getLogger(AppClientMethodExecutor.class.getName());
    private AppClientCmd appClient;
    private AppClientProtocolConfiguration config;
    @Inject
    @DeploymentScoped
    private Instance<Deployment> deploymentInstance;
    @Inject
    @DeploymentScoped
    private Instance<AppClientArchiveName> appClientArchiveName;

    static enum MainStatus {
        PASSED,
        FAILED,
        ERROR,
        NOT_RUN;

        static MainStatus parseStatus(String reason) {
            MainStatus status = FAILED;
            if (reason.contains("Passed.")) {
                status = PASSED;
            } else if (reason.contains("Error.")) {
                status = ERROR;
            } else if (reason.contains("Not run.")) {
                status = NOT_RUN;
            }
            return status;
        }
    }

    public AppClientMethodExecutor(AppClientCmd appClient, AppClientProtocolConfiguration config) {
        this.appClient = appClient;
        this.config = config;
    }

    @Override
    public TestResult invoke(TestMethodExecutor testMethodExecutor) {
        TestResult result = TestResult.passed();

        // Run the appclient for the test if required
        String testMethod = testMethodExecutor.getMethodName();
        if (config.isRunClient()) {
            log.info("Running appClient for: " + testMethod);
            try {
                Deployment deployment = deploymentInstance.get();
                String appArchiveName = appClientArchiveName.get().name();
                String vehicleArchiveName = TsTestPropsBuilder.vehicleArchiveName(deployment);
                String[] additionalAgrs = TsTestPropsBuilder.runArgs(config, deployment, testMethodExecutor);
                appClient.run(vehicleArchiveName, appArchiveName, additionalAgrs);
            } catch (Exception ex) {
                result = TestResult.failed(ex);
                return result;
            }
        } else {
            log.info("Not running appClient for: " + testMethod);
        }
        String[] lines = appClient.readAll(config.getClientTimeout());

        log.info(String.format("AppClient(%s) readAll returned %d lines\n", testMethod, lines.length));
        boolean sawStatus = false;
        MainStatus status = MainStatus.NOT_RUN;
        String reason = "None";
        String description = "None";
        for (String line : lines) {
            System.out.println(line);
            if (line.contains("STATUS:")) {
                sawStatus = true;
                description = line;
                status = MainStatus.parseStatus(line);
                // Format of line is STATUS:StatusText.Reason
                // see com.sun.javatest.Status#exit()
                int reasonStart = line.indexOf('.');
                if (reasonStart > 0 && reasonStart < line.length() - 1) {
                    reason = line.substring(reasonStart + 1);
                }
            }
        }
        if (!sawStatus) {
            Throwable ex = new IllegalStateException("No STATUS: output seen from client");
            result = TestResult.failed(ex);
        } else {
            switch (status) {
                case PASSED:
                    result = TestResult.passed(reason);
                    break;
                case ERROR:
                case FAILED:
                    result = TestResult.failed(new Exception(reason));
                    break;
                case NOT_RUN:
                    result = TestResult.skipped(reason);
                    break;
            }
            result.addDescription(description);
        }

        return result;
    }
}

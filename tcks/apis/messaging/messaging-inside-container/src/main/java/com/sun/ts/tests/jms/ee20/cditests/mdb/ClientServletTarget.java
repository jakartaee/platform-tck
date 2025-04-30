package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.vehicle.none.proxy.ServletNoVehicle;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replaces the remote ejb call used by Client with a servlet call.
 */
@WebServlet(name = "ClientServletTarget", urlPatterns = {"/appclient_novehicle"})
public class ClientServletTarget extends ServletNoVehicle {
    private static final Logger log = Logger.getLogger(ClientServletTarget.class.getName());
    @Inject
    Client client;

    @Override
    protected RemoteStatus runTest() {
        RemoteStatus status;
        try {
            log.info("ClientServletTarget @Client: " + client);
            switch (testName) {
                case "testCDIInjectionOfMDBWithQueueReplyFromEjb" -> client.testCDIInjectionOfMDBWithQueueReplyFromEjb();
                case "testCDIInjectionOfMDBWithTopicReplyFromEjb" -> client.testCDIInjectionOfMDBWithTopicReplyFromEjb();
                default -> throw new IllegalArgumentException("Invalid test name: " + testName);
            };
            status = new RemoteStatus(Status.passed(testName));
        } catch (Exception e) {
            log.log(Level.SEVERE, "runTest failed", e);
            status = new RemoteStatus(Status.failed(testName), e);
        }
        return status;
    }
}

package com.sun.ts.tests.connector.annotations.mdcomplete.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.vehicle.none.proxy.ServletNoVehicle;
import com.sun.ts.tests.connector.annotations.mdcomplete.Client;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;

import java.util.Properties;
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
    protected RemoteStatus setup(String[] args, Properties props) {
        RemoteStatus status;
        try {
            log.info("ClientServletTarget @Inject: " + client);
            client.setup(args, props);
            status = new RemoteStatus(Status.passed("setup"));
        } catch (Exception e) {
            log.log(Level.SEVERE, "setup failed", e);
            status = new RemoteStatus(Status.failed("setup"), e);
        }
        return status;
    }

    @Override
    protected RemoteStatus runTest() {
        RemoteStatus status;
        try {
            log.info("ClientServletTarget @Client: " + client);
            switch (testName) {
                case "testMDCompleteConfigProp" -> client.testMDCompleteConfigProp();
                case "testMDCompleteMCFAnno" -> client.testMDCompleteMCFAnno();

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

package com.sun.ts.tests.connector.annotations.mdcomplete.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy;

import java.util.Properties;

/**
 * The InvocationProxy that is used by the AppClient to communicate with the server side ClientServletTarget
 */
public class IClientProxy extends AbstractBaseProxy<IClient, IClientProxy> {
    public void setup(String[] args, Properties props) {
        IClient client = getLastProxy();
        client.setup(args, props);
    }
    public RemoteStatus runTest(String testName) {
        IClient client = getLastProxy();
        RemoteStatus status = switch (testName) {
            case "testMDCompleteConfigProp" -> client.testMDCompleteConfigProp();
            case "testMDCompleteMCFAnno" -> client.testMDCompleteMCFAnno();

            default -> throw new IllegalArgumentException("Invalid test name: " + testName);
        };
        return status;
    }
}

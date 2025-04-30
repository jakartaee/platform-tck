package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy;

/**
 * The InvocationProxy that is used by the AppClient to communicate with the server side ClientServletTarget
 */
public class IClientProxy extends AbstractBaseProxy<IClient, IClientProxy> {
    public RemoteStatus runTest(String testName) {
        IClient client = getLastProxy();
        RemoteStatus status = switch (testName) {
            case "testCDIInjectionOfMDBWithQueueReplyFromEjb" -> client.testCDIInjectionOfMDBWithQueueReplyFromEjb();
            case "testCDIInjectionOfMDBWithTopicReplyFromEjb" -> client.testCDIInjectionOfMDBWithTopicReplyFromEjb();
            default -> throw new IllegalArgumentException("Invalid test name: " + testName);
        };
        return status;
    }
}

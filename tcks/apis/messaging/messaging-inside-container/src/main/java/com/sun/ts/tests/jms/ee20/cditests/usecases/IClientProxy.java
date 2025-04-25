package com.sun.ts.tests.jms.ee20.cditests.usecases;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy;

/**
 * The InvocationProxy that is used by the AppClient to communicate with the server side ClientServletTarget
 */
public class IClientProxy extends AbstractBaseProxy<IClient, IClientProxy> {
    public RemoteStatus runTest(String testName) {
        IClient client = getLastProxy();
        RemoteStatus status = switch (testName) {
            case "beanUseCaseA" -> client.beanUseCaseA();
            case "beanUseCaseB" -> client.beanUseCaseB();
            case "beanUseCaseC" -> client.beanUseCaseC();
            case "beanUseCaseD" -> client.beanUseCaseD();
            case "beanUseCaseE" -> client.beanUseCaseE();
            case "beanUseCaseF" -> client.beanUseCaseF();
            case "beanUseCaseG" -> client.beanUseCaseG();
            case "beanUseCaseH" -> client.beanUseCaseH();
            case "beanUseCaseJ" -> client.beanUseCaseJ();
            case "beanUseCaseK" -> client.beanUseCaseK();

            default -> throw new IllegalArgumentException("Invalid test name: " + testName);
        };
        return status;
    }
}

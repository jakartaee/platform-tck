package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.lang.reflect.InvocationHandler;

/**
 * The InvocationProxy interface that is used by the AppClient to communicate with the server side ClientServletTarget
 * @param <I> The proxy interface type
 */
public interface InvocationProxy<I> extends InvocationHandler {

    /**
     * Get the I.class type of the proxy interface
     * @return - the class type of the proxy interface
     */
    Class<I> getInterfaceClass();

    /**
     * Creates a new proxy instance of the interface type I that is connected to the given server host/port.
     * @param host - hostname or address
     * @param port - host port
     * @return - a new proxy instance
     */
    I newProxy(String host, String port);
    /**
     * Run the given test method.
     * @return - The test status
     */
    RemoteStatus runTest(String testName);
}

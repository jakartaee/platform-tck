package com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.lang.reflect.InvocationHandler;

public interface InvocationProxy<I> extends InvocationHandler {

    Class<I> getInterfaceClass();
    I newProxy(String host, String port);
    RemoteStatus runTest(String testName);
}

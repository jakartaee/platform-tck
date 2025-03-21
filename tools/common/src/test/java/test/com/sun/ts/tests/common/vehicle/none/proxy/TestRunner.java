package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

public class TestRunner {
    public void runTests() {
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample example = proxyHandler.newProxy(new ExampleTest());
        RemoteStatus status = example.test1();
        System.out.println(status);
        status = example.test1Ex();
        System.out.println(status);
        status = example.test2();
        System.out.println(status);
    }
}

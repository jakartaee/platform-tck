package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.io.IOException;

public class TestHttpRunner {
    public void runTests() throws IOException {
        MockHttpServer server = new MockHttpServer(new ExampleTest());
        server.run(8191);
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample example = proxyHandler.newProxy("localhost", "8191");
        RemoteStatus status = example.test1();
        System.out.println(status);
        status = example.test1Ex();
        System.out.println(status);
        status = example.test2();
        System.out.println(status);
        server.stop();
    }
}

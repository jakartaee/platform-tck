package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class TestHttpRunner {
    public void runTests() throws IOException {
        runTests(new String[0]);
    }
    public void runTests(String[] args) throws IOException {
        runTests(args, null);
    }
    public void runTests(String[] args, Properties props) throws IOException {
        MockHttpServer server;
        if(args.length == 0) {
            server = new MockHttpServer(new ExampleTest());
        } else {
            server = new MockHttpServer(new ExampleTestWithSetup());
        }
        server.run(8191);
        ExampleProxyHandler proxyHandler = new ExampleProxyHandler();
        IExample example = proxyHandler.newProxy("localhost", "8191");
        RemoteStatus status;
        if(props != null) {
            System.out.println("Calling setup, args=" + Arrays.toString(args));
            status = example.setup(args, props);
        } else {
            System.out.println("Calling setup, args=" + Arrays.toString(args));
            status = example.setup(args);
        }
        System.out.println(status);
        if(status.hasError()) {
            rethrowStatus(status);
        }
        status = example.test1();
        System.out.println(status);
        if(status.hasError()) {
            rethrowStatus(status);
        }
        status = example.test1Ex();
        System.out.println(status);
        status = example.test2();
        System.out.println(status);
        if(status.hasError()) {
            rethrowStatus(status);
        }
        server.stop();
    }
    void rethrowStatus(RemoteStatus status) throws IOException {
        IOException testEx = new IOException(status.getErrorMessage());
        testEx.setStackTrace(status.getErrorTrace());
        throw testEx;
    }
}

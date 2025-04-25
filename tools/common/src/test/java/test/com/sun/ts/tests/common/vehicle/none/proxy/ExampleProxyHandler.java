package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy;

import java.util.Properties;

public class ExampleProxyHandler extends AbstractBaseProxy<IExample, ExampleProxyHandler> {
    public void setup(String[] args) {
        IExample client = getLastProxy();
        client.setup(args);
    }
    public void setup(String[] args, Properties props) {
        IExample client = getLastProxy();
        client.setup(args);
    }
    public RemoteStatus runTest(String testName) {
        IExample client = getLastProxy();
        RemoteStatus status = switch (testName) {
            case "test1" -> client.test1();
            case "test1Ex" -> client.test1Ex();
            case "test2" -> client.test2();
            default -> throw new IllegalArgumentException("Invalid test name: " + testName);
        };
        return status;
    }
}

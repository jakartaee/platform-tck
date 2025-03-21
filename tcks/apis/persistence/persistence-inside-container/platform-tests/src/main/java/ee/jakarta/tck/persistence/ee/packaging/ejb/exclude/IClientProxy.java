package ee.jakarta.tck.persistence.ee.packaging.ejb.exclude;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.none.proxy.AbstractBaseProxy;

/**
 * The InvocationProxy that is used by the AppClient to communicate with the server side ClientServletTarget
 */
public class IClientProxy extends AbstractBaseProxy<IClient, IClientProxy> {
    public RemoteStatus runTest(String testName) {
        IClient client = getLastProxy();
        RemoteStatus status = switch (testName) {
            case "test1" -> client.test1();
            case "test2" -> client.test2();

            default -> throw new IllegalArgumentException("Invalid test name: " + testName);
        };
        return status;
    }
}
